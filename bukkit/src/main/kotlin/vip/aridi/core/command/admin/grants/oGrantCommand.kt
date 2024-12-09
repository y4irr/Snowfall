package vip.aridi.core.command.admin.grants

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.module.SharedManager
import vip.aridi.core.module.impl.core.ProfileModule
import vip.aridi.core.profile.Profile
import vip.aridi.core.rank.Rank
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.Duration

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

class oGrantCommand {

    @Command(name = "", desc = "Set grant to a player via console")
    fun defaults(
        @Sender sender: CommandSender,
        target: Profile,
        rank: Rank,
        duration: Duration,
        reason: String
    ) {
        if (sender is Player) {
            sender.sendMessage(CC.translate("&cThis command cannot be executed by users! Console Only!"))
            return
        }
        SharedManager.grantModule.grant(rank, target.id, ProfileModule.CONSOLE_ID, reason, duration.get())

    }
}