package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.OptArg
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.ItemUtils

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class GiveCommand {

    @Command(
        name = "item",
        desc = "Spawn yourself an item.",
        aliases = ["i", "get"],
        usage = "<item> [amount]"
    )
    @Require("snowfall.essentials.item")
    fun item(
        @Sender sender: Player,
        item: ItemStack,
        @OptArg("64") amount: Int
    ) {
        if (!validateAmount(sender, amount)) return

        item.amount = amount
        sender.inventory.addItem(item)
        sender.sendMessage(CC.translate("&6Giving &f$amount &6of &f${ItemUtils.getName(item)}&6."))
    }

    @Command(
        name = "give",
        desc = "Spawn a player an item.",
        aliases = [],
        usage = "<player> <item> [amount]"
    )
    @Require("snowfall.essentials.give")
    fun give(
        @Sender sender: Player,
        target: Player,
        item: ItemStack,
        @OptArg("1") amount: Int
    ) {
        if (!validateAmount(sender, amount)) return

        item.amount = amount
        target.inventory.addItem(item)
        sender.sendMessage(CC.translate("&6Giving &f${target.displayName} &f$amount &6of &f${ItemUtils.getName(item)}&6."))
    }

    private fun validateAmount(sender: Player, amount: Int): Boolean {
        if (amount < 1) {
            sender.sendMessage(CC.translate("&cThe amount must be greater than zero."))
            return false
        }
        return true
    }
}
