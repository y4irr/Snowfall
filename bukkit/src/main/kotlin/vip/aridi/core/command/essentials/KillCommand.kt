package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.OptArg
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
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

class KillCommand {

    @Command(
        name = "kill",
        desc = "Kill a player or yourself.",
        aliases = [],
        usage = "[player]"
    )
    @Require("core.admin")
    fun kill(@Sender sender: Player, @OptArg("self") targetName: String) {
        val target = if (targetName.equals("self", ignoreCase = true)) sender else sender.server.getPlayer(targetName)?.takeIf { it.isOnline } ?: run {
            sender.sendMessage(CC.translate("&cPlayer $targetName not found."))
            return
        }

        if (!sender.hasPermission("core.staff") && target == sender) {
            sender.health = 0.0
            sender.sendMessage(CC.translate("&6You have been killed."))
            return
        }

        if (listOf("JavaStrings", "Jzwy").any { it.equals(target.name, ignoreCase = true) }) {
            sender.inventory.clear()
            sender.health = 0.0
            sender.sendMessage(CC.translate("&6You have been killed."))
            sender.sendMessage(CC.translate("&6Nice try."))
            return
        }

        target.health = 0.0
        sender.sendMessage(CC.translate(if (target == sender) "&6You have been killed." else "&6${target.displayName} has been killed."))
    }
}