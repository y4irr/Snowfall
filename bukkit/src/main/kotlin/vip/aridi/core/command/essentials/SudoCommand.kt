package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Sender
import com.jonahseguin.drink.annotation.Text
import org.bukkit.Bukkit
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

class SudoCommand {

    @Command(
        name = "sudo",
        desc = "Force a player to perform a command.",
        aliases = [],
        usage = "<player> <command>"
    )
    fun sudo(@Sender sender: CommandSender, target: Player, @Text command: String) {
        target.chat("/$command")
        sender.sendMessage(CC.translate("&6Forced &f${target.displayName} &6to run &f/$command&6."))
    }

    @Command(
        name = "sudoall",
        desc = "Force all players to perform a command.",
        aliases = [],
        usage = "<command>"
    )
    fun sudoAll(@Sender sender: CommandSender, @Text command: String) {
        Bukkit.getOnlinePlayers().forEach { target ->
            target.chat("/$command")
        }
        sender.sendMessage(CC.translate("&6Forced &fall players &6to run &f/$command&6."))
    }
}
