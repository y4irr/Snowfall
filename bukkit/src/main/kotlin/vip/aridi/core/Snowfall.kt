package vip.aridi.core

import vip.aridi.core.module.ModuleLifecycleManager
import vip.aridi.core.module.BukkitManager
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
        moduleLifecycleManager = BukkitManager()
    }

    override fun onDisable() {

    }

    companion object {
        fun get(): Snowfall {
            return getPlugin(Snowfall::class.java)
        }
    }
}