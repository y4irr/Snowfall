package vip.aridi.core.utils

import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.inventory.ItemStack

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 25 - nov
 */

enum class EnchantmentWrapper(val friendlyName: String, val parse: Array<String>) {
    PROTECTION_ENVIRONMENTAL("Protection", arrayOf("p", "prot", "protect")),
    PROTECTION_FIRE("Fire Protection", arrayOf("fp", "fprot", "fireprot", "fireprotection", "firep")),
    PROTECTION_FALL("Feather Falling", arrayOf("ff", "featherf", "ffalling")),
    PROTECTION_EXPLOSIONS("Blast Protection", arrayOf("explosionsprotection", "explosionprotection", "bprotection", "bprotect", "blastprotect", "pe", "bp")),
    PROTECTION_PROJECTILE("Projectile Protection", arrayOf("pp", "projprot", "projprotection", "projp", "pprot")),
    THORNS("Thorns", arrayOf()),
    DURABILITY("Unbreaking", arrayOf("unbr", "unb", "dur", "dura")),
    DAMAGE_ALL("Sharpness", arrayOf("s", "sharp")),
    DAMAGE_UNDEAD("Smite", arrayOf("du", "dz")),
    DAMAGE_ARTHROPODS("Bane of Arthropods", arrayOf("bane", "ardmg", "baneofarthropod", "arthropod", "dar", "dspider")),
    KNOCKBACK("Knockback", arrayOf("k", "knock", "kb")),
    FIRE_ASPECT("Fire Aspect", arrayOf("fire", "fa")),
    OXYGEN("Respiration", arrayOf("oxygen", "breathing", "o", "breath")),
    WATER_WORKER("Aqua Affinity", arrayOf("aa")),
    LOOT_BONUS_MOBS("Looting", arrayOf("moblooting", "ml", "loot")),
    DIG_SPEED("Efficiency", arrayOf("e", "eff", "digspeed", "ds")),
    SILK_TOUCH("Silk Touch", arrayOf("silk", "st")),
    LOOT_BONUS_BLOCKS("Fortune", arrayOf("fort", "lbm")),
    DEPTH_STRIDER("Depth Strider", arrayOf("waterwalk", "ds", "depth", "strider")),
    ARROW_DAMAGE("Power", arrayOf("apower", "adamage", "admg")),
    ARROW_KNOCKBACK("Punch", arrayOf("akb", "arrowkb", "arrowknockback", "aknockback")),
    ARROW_FIRE("Fire", arrayOf("afire", "arrowfire")),
    ARROW_INFINITE("Infinity", arrayOf("infinitearrows", "infinite", "inf", "infarrows", "unlimitedarrows", "ai", "uarrows", "unlimited")),
    LUCK("Luck of the Sea", arrayOf("rodluck", "luckofsea", "los")),
    LURE("Lure", arrayOf("rodlure"));

    val bukkitEnchantment: Enchantment
        get() = Enchantment.getByName(this.name) ?: throw IllegalStateException("Enchantment not found: $name")

    val maxLevel: Int
        get() = bukkitEnchantment.maxLevel

    val startLevel: Int
        get() = bukkitEnchantment.startLevel

    val itemTarget: EnchantmentTarget
        get() = bukkitEnchantment.itemTarget

    fun enchant(item: ItemStack, level: Int) {
        item.addUnsafeEnchantment(bukkitEnchantment, level)
    }

    fun conflictsWith(enchantment: Enchantment): Boolean = bukkitEnchantment.conflictsWith(enchantment)

    fun canEnchantItem(item: ItemStack): Boolean = bukkitEnchantment.canEnchantItem(item)

    override fun toString(): String = bukkitEnchantment.toString()

    companion object {
        @JvmStatic
        fun parse(input: String): EnchantmentWrapper? {
            val normalizedInput = input.replace("_", "", true).lowercase()
            return values().firstOrNull { wrapper ->
                wrapper.parse.any { it.equals(normalizedInput, true) }
                        || wrapper.bukkitEnchantment.name.equals(normalizedInput, true)
                        || wrapper.bukkitEnchantment.name.replace("_", "", true).equals(normalizedInput, true)
                        || wrapper.friendlyName.equals(normalizedInput, true)
            }
        }

        @JvmStatic
        fun parse(enchantment: Enchantment): EnchantmentWrapper {
            return values().firstOrNull { it.bukkitEnchantment == enchantment }
                ?: throw IllegalArgumentException("Invalid enchantment given for parsing: ${enchantment.name}")
        }
    }
}