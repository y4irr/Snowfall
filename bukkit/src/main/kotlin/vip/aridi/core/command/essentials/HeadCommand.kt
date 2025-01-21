package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.OptArg
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class HeadCommand {

    @Command(
        name = "head",
        desc = "Spawn yourself a player's head.",
        aliases = ["skull", "skullitem", "giveskull"],
        usage = "[name]"
    )
    @Require("core.staff")
    fun head(@Sender sender: Player, @OptArg("self") name: String) {
        val playerName = if (name == "self") sender.name else name

        val item = ItemStack(Material.SKULL_ITEM, 1, 3.toShort()).apply {
            itemMeta = (itemMeta as SkullMeta).apply {
                owner = playerName
            }
        }

        sender.inventory.addItem(item)
        sender.sendMessage(CC.translate("&6You were given &f$playerName&6's head."))
    }
}
