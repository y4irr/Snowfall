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

        const val UPDATE_PERMISSION = "IlgHL-G5aAjB-ADp1O9l-zDD"
        const val UPDATE_NAME = "U4WJ9-Ypw4XO-61cSk7t-N2R"

        const val EXECUTE_GRANT = "20EPQ-Jpkqt2-qw2Unwl-bx9"
        const val REMOVE_GRANT = "s5HBe-Xt3tUF-u3xk4s6-y1Q"

        const val CREATE_RANK = "zclKa-2nsSd9-hWO17fh-3kP"
        const val DELETE_RANK = "3D3QN-80ktLT-S7TJkbg-fO9"
        const val UPDATE_RANK = "1f8nU-KF7RVI-ARl1rKp-K1V"
    }
}