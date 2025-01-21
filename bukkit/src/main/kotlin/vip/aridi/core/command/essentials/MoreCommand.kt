package vip.aridi.core.command.essentials


import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.OptArg
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class MoreCommand {

    @Command(
        name = "",
        desc = "Give yourself more of the item you're holding.",
        aliases = [],
        usage = "[amount]"
    )
    @Require("core.admin")
    fun more(@Sender sender: Player, @OptArg("64") amount: Int) {
        val itemInHand: ItemStack? = sender.itemInHand

        if (itemInHand == null || itemInHand.type == org.bukkit.Material.AIR) {
            sender.sendMessage(CC.translate("&cYou must be holding an item."))
            return
        }

        itemInHand.amount = (itemInHand.amount + amount).coerceAtMost(64)
        sender.sendMessage(CC.translate("&6There you go."))
    }
}

