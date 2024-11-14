package vip.aridi.core.utils

import org.bukkit.ChatColor

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

object CC {
    @JvmStatic
    fun translate(string: String): String {
        return ChatColor.translateAlternateColorCodes('&', string)
    }
}