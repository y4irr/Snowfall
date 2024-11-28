package vip.aridi.core

import vip.aridi.core.module.ModuleLifecycleManager
import vip.aridi.core.module.ModuleManager
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

        const val UPDATE_PERMISSION: String = "IlgHL-G5aAjB-ADp1O9l-zDD"
        const val UPDATE_NAME: String = "U4WJ9-Ypw4XO-61cSk7t-N2R"
    }
}