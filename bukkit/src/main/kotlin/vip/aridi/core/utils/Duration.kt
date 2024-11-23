package vip.aridi.core.utils

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

class Duration(private var time: Long) {

    fun get(): Long {
        return time
    }

    fun isPermanent(): Boolean {
        return time == Long.MAX_VALUE
    }

    companion object {
        @JvmStatic
        val NOT_PROVIDED = Duration(-1L)

        @JvmStatic
        fun parse(input: String): Duration {
            if (input == "NOT_PROVIDED") {
                return NOT_PROVIDED
            }

            if (input.equals("perm", ignoreCase = true) || input.equals("permanent", ignoreCase = true)) {
                return Duration(0L)
            }

            return Duration(
                DateUtil.parseDuration(
                    input
                )
            )
        }
    }

}