package vip.aridi.core.star

import com.google.gson.JsonObject
import mkremins.fanciful.FancyMessage
import org.bukkit.ChatColor
import vip.aridi.core.Snowfall
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.module.system.PunishmentModule
import vip.aridi.core.punishments.PunishmentData
import vip.aridi.core.punishments.PunishmentType
import vip.aridi.star.listener.StarListener
import vip.aridi.star.stellar.StellarEvent
import vip.aridi.core.utils.TimeUtil
import java.util.*
import kotlin.collections.ArrayList

class StarPunishmentListener: StarListener {

    @StellarEvent(SharedManager.PUNISH_ID)
    fun punish(data: JsonObject) {
        val punishment = SharedManager.databaseModule.gson.fromJson(data, PunishmentData::class.java)
        val pardoned = punishment.isPardoned()
        val silent = if (pardoned) punishment.pardonedSilent else punishment.silent

        val fancyMessage = FancyMessage("${data["victimDisplay"].asString}${ChatColor.GREEN} has been${ChatColor.YELLOW}${if (silent) " silently" else ""}${ChatColor.GREEN} ${if (pardoned) "un" else ""}${punishment.type.context} by ${data["senderDisplay"].asString}${ChatColor.GREEN}.")

        fancyMessage.tooltip(tooltip(punishment))

        Snowfall.get().server.scheduler.runTask(Snowfall.get()) {

            Snowfall.get().server.onlinePlayers.filter {

                if (silent) {
                    return@filter it.hasPermission(punishment.type.permission(pardoned))
                }
                return@filter true

            }.forEach {
                if (it.hasPermission(punishment.type.permission(pardoned))) fancyMessage.send(it) else it.sendMessage(fancyMessage.toOldMessageFormat())
            }

        }

        fancyMessage.send(Snowfall.get().server.consoleSender)

        if (!punishment.type.kickOnExecute) {
            val victim = Snowfall.get().server.getPlayer(punishment.victim)

            if (victim != null) {
                val message = if (punishment.isPardoned() && punishment.type == PunishmentType.MUTE) "${ChatColor.RED}You are no longer silenced." else getPunishmentMessage(punishment)
                if (message != null) {
                    victim.sendMessage(message)
                }

                SharedManager.punishmentModule.mutes[punishment.victim]!!.removeIf { it.id == punishment.id }
                SharedManager.punishmentModule.mutes[punishment.victim]!!.add(punishment)
            } else {
                return
            }

            return
        }

        if (punishment.isPardoned()) {
            return
        }

        if (!punishment.ip) {
            val victim = Snowfall.get().server.getPlayer(punishment.victim)

            if (victim != null) {
                Snowfall.get().server.scheduler.runTask(Snowfall.get()) { victim.kickPlayer(getPunishmentKickMessage(victim.uniqueId, punishment, true)) }
            } else {
                return
            }

            return
        }
        val victimProfile = BukkitManager.profileModule.getProfile(punishment.victim)!!

        val victims = Snowfall.get().server.onlinePlayers.filter { it.uniqueId == punishment.victim ||  victimProfile.address!!.contains(it.address.address.hostAddress)}

        Snowfall.get().server.scheduler.runTask(Snowfall.get()) {
            victims.forEach {
                it.kickPlayer(getPunishmentKickMessage(it.uniqueId, punishment, true))
            }
        }
    }

    private fun tooltip(punishment: PunishmentData): ArrayList<String> {
        val server = "${ChatColor.YELLOW}Server: ${ChatColor.RED}${punishment.server}"
        val reason = "${ChatColor.YELLOW}Reason: ${ChatColor.RED}${punishment.reason}"

        if (punishment.type == PunishmentType.KICK || punishment.type == PunishmentType.WARN || punishment.isPardoned()) {
            return arrayListOf(server, reason)
        }

        val duration = "${ChatColor.YELLOW}Duration: ${ChatColor.RED}${if (punishment.isPermanent()) "Forever" else TimeUtil.formatIntoDetailedString(punishment.duration)}"

        return arrayListOf(server, reason, duration)
    }

    companion object {
        fun getPunishmentMessage(punishment: PunishmentData): String? {
            if (punishment.type == PunishmentType.WARN) {
                return "${ChatColor.RED}You have been warned: ${ChatColor.YELLOW}${punishment.reason}"
            }

            if (punishment.type == PunishmentType.MUTE) {
                return "${ChatColor.RED}${if (punishment.isPermanent()) "You have been permanently muted." else "You have been muted temporally for ${ChatColor.YELLOW}${TimeUtil.formatIntoDetailedString(punishment.duration)}${ChatColor.RED}."}"
            }

            return null
        }

        fun getPunishmentKickMessage(victim: UUID, punishment: PunishmentData, online: Boolean): String {
            if (punishment.type == PunishmentType.KICK) {
                return "${ChatColor.RED}You have been kicked: ${ChatColor.YELLOW}${punishment.reason}"
            }

            var toReturn = if (victim == punishment.victim) "Your account has been ${if (punishment.type == PunishmentType.BLACKLIST) "blacklisted" else "banned"} from ${Snowfall.get().config.getString("SERVER-MANAGER.SERVER-NAME")}" else "Your account has been ${if (punishment.type == PunishmentType.BLACKLIST) "blacklist" else "banned"} due to a punishment related to ${BukkitManager.profileModule.findById(punishment.victim)!!.name}"

            if (!punishment.isPermanent()) {
                toReturn += "\n\n Expires: ${ChatColor.YELLOW}${TimeUtil.formatIntoDetailedString(if (online) punishment.duration else punishment.getRemaining())}"
            }

            return "${ChatColor.RED}$toReturn"
        }
    }
}