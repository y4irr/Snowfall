package vip.aridi.core.command.staff.punishment

import com.jonahseguin.drink.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.module.impl.core.ProfileModule
import vip.aridi.core.punishments.PunishmentType
import vip.aridi.core.utils.Duration

class IPMuteCommand {

    @Command(
        name = "",
        desc = "")
    @Require(
        "snowfall.punishment.ipmute"
    )
    fun punishCommand(
        @Sender sender: CommandSender,
        @Flag('s') silent: Boolean,
        player: Player,
        time: Duration,
        @Text reason: String
    ) {
        val config = BukkitManager.configModule.mainConfig.config.getString("SERVER-MANAGER.SERVER-NAME")

        if (sender is Player) {
            SharedManager.punishmentModule.addPunishment(
                PunishmentType.MUTE,
                player.uniqueId, sender.uniqueId,
                reason,
                config,
                time.get(),
                silent,
                player.name,
                sender.name,
                true,
                BukkitManager.profileModule.getProfile(player.uniqueId)
            )
        } else {
            SharedManager.punishmentModule.addPunishment(
                PunishmentType.MUTE,
                player.uniqueId, ProfileModule.CONSOLE_ID,
                reason,
                config,
                time.get(),
                silent,
                player.name,
                "CONSOLE",
                true,
                BukkitManager.profileModule.getProfile(player.uniqueId)
            )
        }

    }
}