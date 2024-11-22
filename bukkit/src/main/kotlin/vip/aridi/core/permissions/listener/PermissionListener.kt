package vip.aridi.core.permissions.listener

import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.permissions.PermissibleBase
import org.bukkit.plugin.java.JavaPlugin
import vip.aridi.core.listener.oListener
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.permissions.CustomPermissible

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 19 - nov
 */

class PermissionListener(plugin: JavaPlugin) : oListener(plugin) {
    override fun registerEvents() {
        lowestPriority<PlayerLoginEvent> { e ->
            ModuleManager.permissionModule.setPermissible(e.player, CustomPermissible(e.player))
        }

        monitorPriority<PlayerQuitEvent> { e ->
            ModuleManager.permissionModule.setPermissible(e.player, PermissibleBase(e.player))
        }
    }
}