package vip.aridi.core.provider

import com.jonahseguin.drink.argument.CommandArg
import com.jonahseguin.drink.exception.CommandExitMessage
import com.jonahseguin.drink.parametric.DrinkProvider
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import vip.aridi.core.utils.EnchantmentWrapper

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2025
 * Date: 21 - ene
 */

class EnchantmentProvider : DrinkProvider<Enchantment>() {

    override fun doesConsumeArgument(): Boolean = true

    override fun isAsync(): Boolean = false

    override fun allowNullArgument(): Boolean = false

    override fun provide(arg: CommandArg, annotations: MutableList<out Annotation>): Enchantment {
        val source = arg.get()

        val enchantment = EnchantmentWrapper.parse(source)?.bukkitEnchantment
            ?: throw CommandExitMessage("${ChatColor.RED}No enchantment with the name '$source' found.")

        return enchantment
    }

    override fun argumentDescription(): String = "Enchantment"

    override fun getSuggestions(prefix: String): List<String> {
        val completions = mutableListOf<String>()

        EnchantmentWrapper.entries.forEach { enchantment ->
            if (enchantment.parse.any { it.startsWith(prefix, ignoreCase = true) }) {
                completions.addAll(enchantment.parse.filter { it.startsWith(prefix, ignoreCase = true) })
            }
            if (enchantment.friendlyName.startsWith(prefix, ignoreCase = true)) {
                completions.add(enchantment.friendlyName.toLowerCase())
            }
            if (enchantment.bukkitEnchantment.name.startsWith(prefix, ignoreCase = true)) {
                completions.add(enchantment.bukkitEnchantment.name.toLowerCase())
            }
        }

        return completions.distinct()
    }
}