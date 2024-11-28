package vip.aridi.core.command.admin.ranks

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.rank.menus.RankMenu
import vip.aridi.core.rank.menus.editor.RankMetadata

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 23 - nov
 */

class RankCommand {

    @Command(name = "", desc = "Open ranks menu")
    @Require("snowfall.admin.rank.command")
    fun rank(
        @Sender sender: CommandSender
    ) {
        if (sender !is Player) return

        RankMenu().openMenu(sender)
    }
}