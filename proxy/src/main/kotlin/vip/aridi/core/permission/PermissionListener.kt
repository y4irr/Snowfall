package vip.aridi.core.permission

import net.md_5.bungee.api.event.PermissionCheckEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import vip.aridi.core.SnowfallProxy
import vip.aridi.core.module.ProxyManager

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 09 - dic
 */

class PermissionListener
 : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPostLogin(event: PostLoginEvent) {
        ProxyManager.permissionModule.update(event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPermissionCheck(event: PermissionCheckEvent) {
        event.setHasPermission(
            event.sender.permissions
                .contains(event.permission.toLowerCase()) && !event.sender.permissions
                .contains("-${event.permission.toLowerCase()}"))
    }
}