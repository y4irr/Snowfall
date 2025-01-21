package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 25 - nov
 */

class EnchantmentCommand {

    @Command(
        name = "",
        desc = ""
    )
    @Require(
        "snowfall.essentials.enchant"
    )
    fun enchantItem(
        @Sender sender: CommandSender,
        enchantment: Enchantment,
        level: Int
    ) {
        if (!sender ) return
        if (level <= 0) {
            sender.sendMessage(CC.translate("&cThe level must be grater than 0."))
            return
        }

        val item = sender.item

    }
}