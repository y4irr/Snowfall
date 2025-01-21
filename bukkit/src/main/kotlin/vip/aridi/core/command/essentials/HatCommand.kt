package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.*
import org.bukkit.Material
import org.bukkit.command.CommandSender
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

class HatCommand {

    @Command(
        name = "hat",
        desc = "Wear the item in your hand as a hat.",
        usage = ""
    )
    @Require("core.hat")
    fun hat(@Sender sender: CommandSender) {
        val player = sender as? Player ?: return sender.sendMessage(CC.translate("&cOnly players can use this command."))

        val stack = player.itemInHand.takeIf { it != null && it.type != Material.AIR } ?: return player.sendMessage(CC.translate("&cYou are not holding anything."))

        if (stack.type.maxDurability.toInt() != 0) {
            player.sendMessage(CC.translate("&cThe item you are holding is not suitable to wear as a hat."))
            return
        }

        if (player.inventory.helmet?.type != Material.AIR) {
            player.sendMessage(CC.translate("&cYou are already wearing something as your hat."))
            return
        }

        if (stack.amount > 1) stack.amount -= 1 else player.itemInHand = ItemStack(Material.AIR, 1)

        player.inventory.helmet = stack.clone().apply { amount = 1 }
        player.sendMessage(CC.translate("&6You are now wearing &f${stack.type.name.lowercase().replace('_', ' ')} &6as a hat."))
    }
}

