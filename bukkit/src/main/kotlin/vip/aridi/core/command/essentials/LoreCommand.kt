package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.utils.CC
import java.util.concurrent.atomic.AtomicInteger

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class LoreCommand {

    private fun Player.getItemInHandOrNull(): org.bukkit.inventory.ItemStack? {
        val item = this.itemInHand
        if (item == null || item.type == Material.AIR) {
            this.sendMessage(CC.translate("&cYou need to hold an item in your hand."))
            return null
        }
        return item
    }

    private fun org.bukkit.inventory.ItemStack.getItemMetaOrFail(player: Player): org.bukkit.inventory.meta.ItemMeta? {
        val meta = this.itemMeta
        if (meta == null) {
            player.sendMessage(CC.translate("&cThis item has no metadata."))
        }
        return meta
    }

    private fun org.bukkit.inventory.meta.ItemMeta.getLoreOrEmpty(): MutableList<String> {
        return (this.lore ?: ArrayList()).toMutableList()
    }

    private fun Player.updateItem(item: org.bukkit.inventory.ItemStack, meta: org.bukkit.inventory.meta.ItemMeta) {
        item.itemMeta = meta
        this.updateInventory()
    }

    private fun Player.handleLoreAction(action: (MutableList<String>) -> Unit) {
        val item = this.getItemInHandOrNull() ?: return
        val meta = item.getItemMetaOrFail(this) ?: return
        val lore = meta.getLoreOrEmpty()
        action(lore)
        meta.lore = lore
        this.updateItem(item, meta)
    }

    @Command(name = "help", desc = "List all available commands")
    fun help(@Sender sender: CommandSender) {
        if (sender !is Player) return
        sender.sendMessage(CC.translate("&d&lAvailable Commands:"))
        sender.sendMessage(CC.translate("&f/lore <text> &7- Add a new lore to the item in hand."))
        sender.sendMessage(CC.translate("&f/editlore list &7- List all lore lines of the item."))
        sender.sendMessage(CC.translate("&f/editlore addlore <text> &7- Add a lore line to the item."))
        sender.sendMessage(CC.translate("&f/editlore editline <index> <text> &7- Edit a specific lore line."))
        sender.sendMessage(CC.translate("&f/editlore removelore <index> &7- Remove a lore line by its index."))
        sender.sendMessage(CC.translate("&f/help &7- Show this help menu."))
    }

    @Command(name = "lore", desc = "Add lore to the item in hand")
    @Require("snowfall.essentials.lore")
    fun addLore(@Sender sender: CommandSender, name: String) {
        if (sender !is Player) return
        sender.handleLoreAction { lore ->
            lore.add(CC.translate(name))
            sender.sendMessage(CC.translate("&aLore added successfully to the item."))
        }
    }

    @Command(name = "editlore list", desc = "List the item's lore")
    fun listLore(@Sender sender: CommandSender) {
        if (sender !is Player) return
        val item = sender.getItemInHandOrNull() ?: return
        val meta = item.getItemMetaOrFail(sender) ?: return

        sender.sendMessage("")
        sender.sendMessage(CC.translate("&d&lItem Information:"))
        sender.sendMessage(CC.translate("&fName: &d${meta.displayName ?: "None"}"))
        sender.sendMessage(CC.translate("&fLore:"))
        meta.lore?.forEachIndexed { index, line ->
            sender.sendMessage(CC.translate("&7${index}. &f${line}"))
        } ?: sender.sendMessage(CC.translate("&7- This item has no lore."))
        sender.sendMessage("")
    }

    @Command(name = "editlore removelore", desc = "Remove a lore line by index")
    fun removeLore(@Sender sender: CommandSender, line: Int) {
        if (sender !is Player) return
        sender.handleLoreAction { lore ->
            if (line !in lore.indices) {
                sender.sendMessage(CC.translate("&cInvalid lore index."))
                return@handleLoreAction
            }
            lore.removeAt(line)
            sender.sendMessage(CC.translate("&aLore line removed successfully."))
        }
    }

    @Command(name = "editlore addlore", desc = "Add a new lore line to the item")
    fun addLoreLine(@Sender sender: CommandSender, name: String) {
        if (sender !is Player) return
        sender.handleLoreAction { lore ->
            lore.add(CC.translate(name))
            sender.sendMessage(CC.translate("&aNew lore line added successfully."))
        }
    }

    @Command(name = "editlore editline", desc = "Edit a specific lore line")
    fun editLoreLine(@Sender sender: CommandSender, index: Int, name: String) {
        if (sender !is Player) return
        sender.handleLoreAction { lore ->
            if (index !in lore.indices) {
                sender.sendMessage(CC.translate("&cInvalid lore index."))
                return@handleLoreAction
            }
            lore[index] = CC.translate(name)
            sender.sendMessage(CC.translate("&aLore line updated successfully."))
        }
    }
}