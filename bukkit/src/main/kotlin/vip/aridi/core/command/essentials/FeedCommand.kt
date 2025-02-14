package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class FeedCommand {

    @Command(
        name = "",
        desc = "This command feeds you.",
    )
    @Require("snowfall.essentials.feed")
    fun feed(
        @Sender sender: CommandSender,
        target: Player
    ) {
        if (sender !is Player) return

        if (sender != target && !sender.hasPermission("snowfall.essentials.feed")) {
            sender.sendMessage(CC.translate("&cNo permission to feed other players."))
            return
        }

        target.foodLevel = 20
        target.saturation = 10.0f

        if (sender != target) {
            sender.sendMessage(CC.translate("&6${target.displayName} has been fed."))
        }
        target.sendMessage(CC.translate("&6You have been fed."))
    }
}
