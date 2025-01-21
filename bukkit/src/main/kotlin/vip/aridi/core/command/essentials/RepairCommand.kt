package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class RepairCommand {

    @Command(
        name = "",
        desc = "Repair items in your inventory or your hand.",
        usage = "<hand|all>",
    )
    fun repair(
        @Sender sender: CommandSender
    ) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}Only players can use this command.")
            return
        }

        if (sender.hasPermission("core.repair.all")) {
            sender.sendMessage("${ChatColor.RED}/repair <hand|all>")
        } else if (sender.hasPermission("core.repair")) {
            sender.sendMessage("${ChatColor.RED}/repair <hand>")
        } else {
            sender.sendMessage("${ChatColor.RED}You don't have permission to use this command.")
        }
    }

    @Command(
        name = "hand",
        desc = "Repair the item you're currently holding.",
        usage = "",
    )
    fun repairHand(
        @Sender sender: CommandSender
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

        if (item.durability == 0.toShort()) {
            sender.sendMessage("${ChatColor.RED}That ${ChatColor.WHITE}${item.type.name}${ChatColor.RED} already has max durability.")
            return
        }

        item.durability = 0.toShort()
        sender.sendMessage("${ChatColor.GOLD}Your ${ChatColor.WHITE}${item.type.name}${ChatColor.GOLD} has been repaired.")
    }

    @Command(
        name = "all",
        desc = "Repair all items in your inventory.",
        usage = "",
    )
    fun repairAll(
        @Sender sender: CommandSender
    ) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}Only players can use this command.")
            return
        }

        val inventory = sender.inventory
        var repairedItems = 0

        (inventory.contents + inventory.armorContents).forEach { item ->
            if (item != null && isRepairable(item) && item.durability > 0) {
                item.durability = 0.toShort()
                repairedItems++
            }
        }

        sender.sendMessage("${ChatColor.GOLD}Repaired ${ChatColor.WHITE}$repairedItems ${ChatColor.GOLD}items in your inventory.")
    }

    private fun isRepairable(item: ItemStack): Boolean {
        val repairableMaterials = setOf(
            Material.WOOD_AXE, Material.WOOD_HOE, Material.WOOD_SWORD, Material.WOOD_SPADE, Material.WOOD_PICKAXE,
            Material.STONE_AXE, Material.STONE_HOE, Material.STONE_SWORD, Material.STONE_SPADE, Material.STONE_PICKAXE,
            Material.GOLD_AXE, Material.GOLD_HOE, Material.GOLD_SWORD, Material.GOLD_SPADE, Material.GOLD_PICKAXE,
            Material.IRON_AXE, Material.IRON_HOE, Material.IRON_SWORD, Material.IRON_SPADE, Material.IRON_PICKAXE,
            Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_SWORD, Material.DIAMOND_SPADE, Material.DIAMOND_PICKAXE,
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS,
            Material.FISHING_ROD, Material.CARROT_STICK, Material.SHEARS, Material.FLINT_AND_STEEL, Material.BOW
        )
        return item.type in repairableMaterials
    }
}