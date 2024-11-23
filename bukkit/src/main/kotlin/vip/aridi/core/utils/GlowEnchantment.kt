package vip.aridi.core.utils

import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.enchantments.EnchantmentWrapper
import org.bukkit.inventory.ItemStack

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

class GlowEnchantment(id: Int) : EnchantmentWrapper(id) {

    override fun canEnchantItem(item: ItemStack): Boolean {
        return true
    }

    override fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    override fun getItemTarget(): EnchantmentTarget? {
        return null
    }

    override fun getMaxLevel(): Int {
        return 10
    }

    override fun getName(): String {
        return "Glow"
    }

    override fun getStartLevel(): Int {
        return 1
    }

    companion object {

        private var glow: Enchantment? = null

        @JvmStatic
        fun registerEnchant() {
            try {
                val f = Enchantment::class.java.getDeclaredField("acceptingNew")
                f.isAccessible = true
                f.set(null, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            glow = GlowEnchantment(255)

            if (Enchantment.getByName("Glow") == null) {
                Enchantment.registerEnchantment(glow!!)
            }
        }

        @JvmStatic
        fun addGlow(item: ItemStack) {
            if (item.enchantments.isEmpty()) {
                item.addEnchantment(glow, 1)
            } else {
                return
            }
        }

    }

}