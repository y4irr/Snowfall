package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
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

class SuicideCommand {

    @Command(
        name = "",
        desc = ""
    )
    @Require(
        "snowfall.essentials.suicide"
    )
    fun suicide(
        @Sender sender: CommandSender
    ) {
        if (sender !is Player) return
        sender.health = 0.0
        sender.sendMessage(CC.translate("&cYou have been killed."))
    }
}