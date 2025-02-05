package vip.aridi.core.command.staff.punishment

import com.jonahseguin.drink.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.module.impl.core.ProfileModule
import vip.aridi.core.punishments.PunishmentType
import vip.aridi.core.utils.CC

class UnBlacklistCommand {

    @Command(
        name = "",
        desc = "")
    @Require(
        "snowfall.punishment.unblacklist"
    )
    fun pardon(
        @Sender sender: CommandSender,
        @Flag('s') silent: Boolean,
        player: Player,
        @Text reason: String
    ) {
        val punishmentModule = SharedManager.punishmentModule
        val findLastPunishment = punishmentModule.findLastPunishmentByType(player.uniqueId, PunishmentType.BLACKLIST)

        if (findLastPunishment == null) {
            sender.sendMessage(CC.translate("&cThis player is not blacklisted."))
            return
        }

        if (sender is Player) {
            SharedManager.punishmentModule.pardonPunishment(
                findLastPunishment.id,
                sender.uniqueId,
                reason,
                silent,
                player.displayName,
                sender.displayName
            )
        } else {
            SharedManager.punishmentModule.pardonPunishment(
                findLastPunishment.id,
                ProfileModule.CONSOLE_ID,
                reason,
                silent,
                player.displayName,
                "CONSOLE"
            )
        }
    }

}