package vip.aridi.core.listener

import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.event.ServerDisconnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import vip.aridi.core.SnowfallProxy
import vip.aridi.core.module.SharedManager

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 08 - dic
 */

class ProxyListener(
    private val snowfall: SnowfallProxy
): Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onLogin(event: LoginEvent) {
        event.registerIntent(snowfall)

        snowfall.proxy.scheduler.runAsync(snowfall) {
            val grants = SharedManager.grantModule.findAllByPlayer(event.connection.uniqueId).toHashSet()

            event.completeIntent(snowfall)

            SharedManager.grantModule.active[event.connection.uniqueId] = grants.filter { it.isActive() }.toCollection(ArrayList())

            SharedManager.grantModule.setGrant(event.connection.uniqueId, grants)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onServerSwitch(event: ServerConnectedEvent) {
        SharedManager.grantModule.setGrant(event.player.uniqueId, SharedManager.grantModule.active[event.player.uniqueId] ?: HashSet())
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDisconnect(event: PlayerDisconnectEvent) {
        SharedManager.grantModule.active.remove(event.player.uniqueId)
    }
}