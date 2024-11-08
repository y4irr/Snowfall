package net.cyruspvp.net.cyruspvp.core

import org.bukkit.plugin.java.JavaPlugin

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

class Snowfall: JavaPlugin() {

    override fun onEnable() {

    }

    override fun onDisable() {

    }

    companion object {
        fun get(): Snowfall {
            return getPlugin(Snowfall::class.java)
        }
    }
}