package net.cyruspvp.net.cyruspvp.core

import net.cyruspvp.core.module.ModuleLifecycleManager
import net.cyruspvp.core.module.ModuleManager
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

    private lateinit var moduleLifecycleManager: ModuleLifecycleManager

    override fun onEnable() {
        moduleLifecycleManager = ModuleManager(this)
    }

    override fun onDisable() {

    }

    companion object {
        fun get(): Snowfall {
            return getPlugin(Snowfall::class.java)
        }
    }
}