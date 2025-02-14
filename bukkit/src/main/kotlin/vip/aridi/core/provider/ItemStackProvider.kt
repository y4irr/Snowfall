package vip.aridi.core.provider

import com.jonahseguin.drink.argument.CommandArg
import com.jonahseguin.drink.exception.CommandExitMessage
import com.jonahseguin.drink.parametric.DrinkProvider
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


class ItemStackProvider : DrinkProvider<ItemStack>() {

    override fun doesConsumeArgument(): Boolean = true

    override fun isAsync(): Boolean = false

    override fun allowNullArgument(): Boolean = false

    override fun provide(arg: CommandArg, annotations: MutableList<out Annotation>): ItemStack {
        val source = arg.get()
        val parts = source.split(":")
        val material = Material.getMaterial(parts[0].uppercase())
            ?: throw CommandExitMessage("${ChatColor.RED}No item with the name $source found.")

        val durability = parts.getOrNull(1)?.toShortOrNull() ?: 0
        val amount = parts.getOrNull(2)?.toIntOrNull() ?: 1

        return ItemStack(material, amount, durability)
    }

    override fun argumentDescription(): String = "ItemStack (Format: item[:durability][:amount])"

    override fun getSuggestions(prefix: String): List<String> {
        return Material.values()
            .map { it.name }
            .filter { it.startsWith(prefix, ignoreCase = true) }
    }
}
