package vip.aridi.core.command.staff.punishment

import com.jonahseguin.drink.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.module.impl.core.ProfileModule
import vip.aridi.core.punishments.PunishmentType
import vip.aridi.core.utils.Duration

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 26 - dic
 */

class BanCommand {

    @Command(
        name = "",
        desc = "")
    @Require(
        "snowfall.punishment.ban"
    )
    fun punishCommand(
        @Sender sender: CommandSender,
        @Flag('s') silent: Boolean,
        player: Player,
        @OptArg("perm") time: Duration,
        @Text reason: String) {

        val config = BukkitManager.configModule.mainConfig.config.getString("SERVER-MANAGER.SERVER-NAME")

        if (sender is Player) {
            SharedManager.punishmentModule.addPunishment(
                PunishmentType.BAN,
                player.uniqueId, sender.uniqueId,
                reason,
                config,
                time.get(),
                silent,
                player.name,
                sender.name,
                false,
                BukkitManager.profileModule.getProfile(player.uniqueId)
            )
        } else {
            SharedManager.punishmentModule.addPunishment(
                PunishmentType.BAN,
                player.uniqueId, ProfileModule.CONSOLE_ID,
                reason,
                config,
                time.get(),
                silent,
                player.name,
                "CONSOLE",
                false,
                BukkitManager.profileModule.getProfile(player.uniqueId)
            )
        }
    }

}