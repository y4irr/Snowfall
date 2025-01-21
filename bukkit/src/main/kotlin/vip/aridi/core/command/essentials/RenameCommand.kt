package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
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

class RenameCommand {

    private val customNameStarter = ChatColor.translateAlternateColorCodes('&', "&b&c&f")

    @Command(
        name = "rename",
        desc = "Rename the item you're currently holding. Supports color codes.",
        aliases = [],
        usage = "<name>",
    )
    fun rename(
        @Sender sender: CommandSender,
        name: String
    ) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}Only players can use this command.")
            return
        }

        val item = sender.itemInHand
        if (item == null || item.type == Material.AIR) {
            sender.sendMessage("${ChatColor.RED}You must be holding an item.")
            return
        }

        var newName = name
        if (sender.hasPermission("core.rename")) {
            newName = ChatColor.translateAlternateColorCodes('&', name)
        }

        if (!sender.isOp) {
            if (newName.length >= 40) {
                sender.sendMessage("${ChatColor.RED}The maximum characters you can set for a name is 40.")
                return
            }

            val restrictedItems = listOf(
                Material.INK_SACK, Material.TRIPWIRE_HOOK, Material.ENDER_CHEST, Material.TRAPPED_CHEST,
                Material.CHEST, Material.BEACON, Material.ANVIL, Material.HOPPER, Material.HOPPER_MINECART,
                Material.DISPENSER, Material.DROPPER, Material.STORAGE_MINECART, Material.FURNACE
            )
            if (item.type in restrictedItems) {
                sender.sendMessage(CC.translate("&cYou can't rename this item!"))
                return
            }

            if (sender.gameMode != GameMode.CREATIVE) {
                item.itemMeta?.lore?.let { lore ->
                    if (lore.contains(CC.translate("&cUnrepairable"))) {
                        sender.sendMessage(CC.translate("&cYou can't rename this item!"))
                        return
                    }
                }
            }
        }

        val meta = item.itemMeta ?: return
        val isCustomEnchant = meta.hasDisplayName() && meta.displayName.startsWith(customNameStarter)
        meta.displayName =
            if (isCustomEnchant && !newName.startsWith(customNameStarter)) customNameStarter + newName else newName
        item.itemMeta = meta

        sender.updateInventory()
        sender.sendMessage(
            "${ChatColor.GOLD}Renamed your ${ChatColor.WHITE}${ItemUtils.getName(item)}${ChatColor.GOLD} to ${ChatColor.WHITE}$newName${ChatColor.GOLD}."
        )
    }
}