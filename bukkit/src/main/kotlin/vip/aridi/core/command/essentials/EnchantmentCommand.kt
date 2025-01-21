package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Flag
import com.jonahseguin.drink.annotation.OptArg
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.EnchantmentWrapper
import vip.aridi.core.utils.ItemUtils

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
        @Flag('f') hotbar: Boolean,
        enchantment: Enchantment,
        @OptArg("1") level: Int
    ) {
        if (sender !is Player) return
        if (level <= 0) {
            sender.sendMessage(CC.translate("&cThe level must be greater than 0."))
            return
        }

        val wrapper = EnchantmentWrapper.parse(enchantment)

        if (hotbar) enchantHotbar(sender, wrapper, level) else enchantItemInHand(sender, wrapper, level)
    }

    private fun enchantItemInHand(player: Player, wrapper: EnchantmentWrapper, level: Int) {
        val item = player.itemInHand ?: return player.sendMessage(CC.translate("&cYou must be holding an item."))

        if (!validateLevel(player, wrapper, level)) return

        wrapper.enchant(item, level)
        player.updateInventory()
        player.sendMessage(CC.translate("&6Enchanted your &f${ItemUtils.getName(item)}&6 with &f${wrapper.friendlyName}&6 level &f$level&6."))
    }

    private fun enchantHotbar(player: Player, wrapper: EnchantmentWrapper, level: Int) {
        var enchantedCount = 0

        for (slot in 0..8) {
            val item = player.inventory.getItem(slot) ?: continue
            if (!wrapper.canEnchantItem(item)) continue

            if (!validateLevel(player, wrapper, level, warn = false)) return

            wrapper.enchant(item, level)
            enchantedCount++
        }

        if (enchantedCount == 0) {
            player.sendMessage(CC.translate("&cNo items in your hotbar can be enchanted with ${wrapper.friendlyName}."))
            return
        }

        player.sendMessage(CC.translate("&6Enchanted &f$enchantedCount&6 items with &f${wrapper.friendlyName}&6 level &f$level&6."))
        player.updateInventory()
    }

    private fun validateLevel(player: Player, wrapper: EnchantmentWrapper, level: Int, warn: Boolean = true): Boolean {
        if (level <= wrapper.maxLevel) return true

        if (!player.hasPermission("snowfall.enchant.bypass")) {
            player.sendMessage(CC.translate("&cThe maximum enchant level for ${wrapper.friendlyName} is ${wrapper.maxLevel}."))
            return false
        }

        if (warn) {
            player.sendMessage(CC.translate("&c&lWARNING: &eYou added ${wrapper.friendlyName} $level to this item. The default maximum value is ${wrapper.maxLevel}."))
        }

        return true
    }
}