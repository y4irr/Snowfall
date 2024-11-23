package vip.aridi.core.utils

import org.apache.commons.lang.WordUtils
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.DyeColor
import java.util.stream.Collectors

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

object CC  {

    @JvmField val BLUE = ChatColor.BLUE
    @JvmField val AQUA = ChatColor.AQUA
    @JvmField val YELLOW = ChatColor.YELLOW
    @JvmField val RED = ChatColor.RED
    @JvmField val GRAY = ChatColor.GRAY
    @JvmField val GOLD = ChatColor.GOLD
    @JvmField val GREEN = ChatColor.GREEN
    @JvmField val WHITE = ChatColor.WHITE
    @JvmField val BLACK = ChatColor.BLACK
    @JvmField val BOLD = ChatColor.BOLD
    @JvmField val ITALIC = ChatColor.ITALIC
    @JvmField val UNDER_LINE = ChatColor.UNDERLINE
    @JvmField val STRIKE_THROUGH = ChatColor.STRIKETHROUGH
    @JvmField val RESET = ChatColor.RESET
    @JvmField val MAGIC = ChatColor.MAGIC
    @JvmField val DARK_BLUE = ChatColor.DARK_BLUE
    @JvmField val DARK_AQUA = ChatColor.DARK_AQUA
    @JvmField val DARK_GRAY = ChatColor.DARK_GRAY
    @JvmField val DARK_GREEN = ChatColor.DARK_GREEN
    @JvmField val DARK_PURPLE = ChatColor.DARK_PURPLE
    @JvmField val DARK_RED = ChatColor.DARK_RED
    @JvmField val PINK = ChatColor.LIGHT_PURPLE
    @JvmField val MENU_BAR = "${ChatColor.DARK_GRAY}${ChatColor.STRIKETHROUGH}------------------------"
    @JvmField val CHAT_BAR = "${ChatColor.DARK_GRAY}${ChatColor.STRIKETHROUGH }------------------------------------------------"
    @JvmField val SB_BAR = "${ChatColor.DARK_GRAY}${ChatColor.STRIKETHROUGH}----------------------"
    @JvmField val LINE: String = "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}--------------------------"

    @JvmStatic
    fun translate(value: String): String {
        return ChatColor.translateAlternateColorCodes('&', value)
    }

    @JvmStatic
    fun translateList(list: List<String>): List<String> {
        return list.stream().map { translate(it) }.collect(Collectors.toList())
    }

    @JvmStatic
    fun translateMutable(list: MutableList<String>): MutableList<String> {
        return list.stream().map { translate(it) }.collect(Collectors.toList())
    }

    @JvmStatic
    fun translate(list: ArrayList<String>): MutableList<String> {
        return list.stream().map { translate(it) }.collect(Collectors.toList())
    }

    @JvmStatic
    fun convertToChatColor(colorName: String): ChatColor {
        return when (colorName.toLowerCase()) {
            "black" -> ChatColor.BLACK
            "dark blue" -> ChatColor.DARK_BLUE
            "dark green" -> ChatColor.DARK_GREEN
            "dark aqua" -> ChatColor.DARK_AQUA
            "dark red" -> ChatColor.DARK_RED
            "dark purple" -> ChatColor.DARK_PURPLE
            "gold" -> ChatColor.GOLD
            "dark gray" -> ChatColor.DARK_GRAY
            "blue" -> ChatColor.BLUE
            "green" -> ChatColor.GREEN
            "aqua" -> ChatColor.AQUA
            "red" -> ChatColor.RED
            "light purple" -> ChatColor.LIGHT_PURPLE
            "yellow" -> ChatColor.YELLOW
            "white" -> ChatColor.WHITE
            else -> ChatColor.GRAY
        }
    }

    @JvmStatic
    fun convertToChatColorName(colorName: String): String {
        return when (colorName.toLowerCase()) {
            "&0" -> "BLACK"
            "&1" -> "DARK_BLUE"
            "&2" -> "DARK_GREEN"
            "&3" -> "DARK_AQUA"
            "&4" -> "DARK_RED"
            "&5" -> "DARK_PURPLE"
            "&6" -> "GOLD"
            "&7" -> "GRAY"
            "&8" -> "DARK_GRAY"
            "&9" -> "BLUE"
            "&a" -> "GREEN"
            "&b" -> "AQUA"
            "&c" -> "RED"
            "&d" -> "LIGHT_PURPLE"
            "&e" -> "YELLOW"
            "&f" -> "WHITE"
            else -> "BLACK"
        }
    }

    @JvmStatic
    fun convert(color: String): String {
        return try {
            val color1 = ChatColor.valueOf(color.toUpperCase())
            when (color1) {
                ChatColor.BLUE -> "Blue"
                ChatColor.DARK_GRAY -> "Dark Gray"
                ChatColor.GRAY -> "Gray"
                ChatColor.GOLD -> "Gold"
                ChatColor.DARK_PURPLE -> "Dark Purple"
                ChatColor.DARK_RED -> "Dark Red"
                ChatColor.DARK_AQUA -> "Dark Aqua"
                ChatColor.DARK_GREEN -> "Dark Green"
                ChatColor.DARK_BLUE -> "Dark Blue"
                ChatColor.LIGHT_PURPLE -> "Pink"
                ChatColor.YELLOW -> "Yellow"
                ChatColor.RED -> "Red"
                ChatColor.AQUA -> "Aqua"
                ChatColor.WHITE -> "White"
                ChatColor.GREEN -> "Green"
                else -> "Gray"
            }
        } catch (e: IllegalArgumentException) {
            "Invalid color"
        }
    }

    @JvmStatic
    fun getWoolData(colorValue: String): Byte {
        val color = ChatColor.valueOf(colorValue)
        val dyeColor = getDyeColor(color)
        return dyeColor.woolData
    }

    @JvmStatic
    fun getDyeColor(color: ChatColor): DyeColor {
        return when (color) {
            ChatColor.DARK_BLUE, ChatColor.BLUE -> DyeColor.BLUE
            ChatColor.DARK_GREEN -> DyeColor.GREEN
            ChatColor.DARK_AQUA -> DyeColor.CYAN
            ChatColor.AQUA -> DyeColor.LIGHT_BLUE
            ChatColor.DARK_RED, ChatColor.RED -> DyeColor.RED
            ChatColor.DARK_PURPLE -> DyeColor.PURPLE
            ChatColor.GOLD -> DyeColor.ORANGE
            ChatColor.GRAY -> DyeColor.SILVER
            ChatColor.DARK_GRAY -> DyeColor.GRAY
            ChatColor.GREEN -> DyeColor.LIME
            ChatColor.LIGHT_PURPLE -> DyeColor.PINK
            ChatColor.YELLOW -> DyeColor.YELLOW
            ChatColor.WHITE -> DyeColor.WHITE
            else -> DyeColor.BLACK
        }
    }

    @JvmStatic
    val COLOR_TO_NAME = mapOf<Color, String>(
        Color.WHITE to "WHITE",
        Color.RED to "RED",
        Color.BLUE to "BLUE",
        Color.AQUA to "AQUA",
        Color.PURPLE to "DARK_PURPLE",
        Color.FUCHSIA to "LIGHT_PURPLE",
        Color.ORANGE to "GOLD",
        Color.YELLOW to "YELLOW",
        Color.LIME to "GREEN",
        Color.GREEN to "DARK_GREEN",
        Color.SILVER to "GRAY",
        Color.GRAY to "DARK_GRAY",
        Color.MAROON to "DARK_RED",
        Color.NAVY to "DARK_AQUA",
        Color.BLACK to "BLACK"
    )

    @JvmStatic
    val COLOR_TO_CHAT_COLOR = mapOf<Color, ChatColor>(
        Color.WHITE to ChatColor.WHITE,
        Color.RED to ChatColor.RED,
        Color.BLUE to ChatColor.BLUE,
        Color.AQUA to ChatColor.AQUA,
        Color.PURPLE to ChatColor.DARK_PURPLE,
        Color.FUCHSIA to ChatColor.LIGHT_PURPLE,
        Color.ORANGE to ChatColor.GOLD,
        Color.YELLOW to ChatColor.YELLOW,
        Color.LIME to ChatColor.GREEN,
        Color.GREEN to ChatColor.DARK_GREEN,
        Color.SILVER to ChatColor.GRAY,
        Color.GRAY to ChatColor.DARK_GRAY,
        Color.MAROON to ChatColor.DARK_RED,
        Color.NAVY to ChatColor.DARK_AQUA,
        Color.BLACK to ChatColor.BLACK
    )

    @JvmStatic
    val CHAT_COLOR_TO_DYE_DATA = mapOf(
        ChatColor.WHITE to 15,
        ChatColor.GOLD to 14,
        ChatColor.AQUA to 12,
        ChatColor.YELLOW to 11,
        ChatColor.GREEN to 10,
        ChatColor.LIGHT_PURPLE to 9,
        ChatColor.GRAY to 8,
        ChatColor.DARK_GRAY to 7,
        ChatColor.DARK_AQUA to 6,
        ChatColor.DARK_PURPLE to 5,
        ChatColor.BLUE to 4,
        ChatColor.DARK_GREEN to 2,
        ChatColor.RED to 1,
        ChatColor.DARK_RED to 1,
        ChatColor.BLACK to 0
    )

    @JvmStatic
    val CHAT_COLOR_TO_WOOL_DATA = mapOf(
        ChatColor.DARK_RED to 14,
        ChatColor.RED to 14,
        ChatColor.GOLD to 1,
        ChatColor.YELLOW to 4,
        ChatColor.GREEN to 5,
        ChatColor.DARK_GREEN to 13,
        ChatColor.DARK_AQUA to 9,
        ChatColor.AQUA to 3,
        ChatColor.BLUE to 11,
        ChatColor.DARK_PURPLE to 10,
        ChatColor.LIGHT_PURPLE to 2,
        ChatColor.WHITE to 0,
        ChatColor.GRAY to 8,
        ChatColor.DARK_GRAY to 7,
        ChatColor.BLACK to 15
    )

    @JvmStatic
    fun getPredefinedColors(): Set<Color> {
        return COLOR_TO_NAME.keys
    }

    @JvmStatic
    fun toChatColor(color: Color): ChatColor {
        return COLOR_TO_CHAT_COLOR[color] ?: ChatColor.WHITE
    }

    @JvmStatic
    fun getColorName(color: Color): String {
        return COLOR_TO_NAME[color] ?: "Custom"
    }

    @JvmStatic
    fun toDyeData(color: ChatColor): Int {
        return CHAT_COLOR_TO_DYE_DATA[color] ?: 0
    }

    @JvmStatic
    fun toWoolData(color: ChatColor): Int {
        return CHAT_COLOR_TO_WOOL_DATA[color] ?: 0
    }

    @JvmStatic
    fun getChatColorByWoolId(data: Byte): ChatColor? {
        for ((key, value) in CHAT_COLOR_TO_WOOL_DATA) {
            if (value == data.toInt()) {
                return key
            }
        }
        return null
    }

    @JvmStatic
    fun getWoolName(data: Byte): String {
        val color = getChatColorByWoolId(data)
        return WordUtils.capitalizeFully((color?.name ?: "UNKNOWN").replace("_", " "))
    }

    @JvmStatic
    fun convertColorValue(value: Double): Double {
        var value = value
        if (value <= 0.0) {
            value = -1.0
        }
        return value / 255.0
    }
}