package vip.aridi.core.permissions.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.permissions.PermissibleBase
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.plugin.java.JavaPlugin
import vip.aridi.core.Snowfall
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.permissions.CustomPermissible
import java.util.*

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 19 - nov
 */

class PermissionListener : Listener {
    private val attachments = mutableMapOf<UUID, PermissionAttachment>()

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerLogin(event: PlayerLoginEvent) {
        val player = event.player

        val attachment = attachments[player.uniqueId] ?: player.addAttachment(Snowfall.get()).apply {
            attachments[player.uniqueId] = this
        }

        val grant = SharedManager.grantModule.findGrantedRank(player.uniqueId)
        grant.permissions.forEach {
            if (it == "*") player.isOp = true
            attachment.setPermission(it, true)
        }

        player.recalculatePermissions()
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        val attachment = attachments.remove(player.uniqueId)
        if (attachment != null) {
            val grant = SharedManager.grantModule.findGrantedRank(player.uniqueId)
            grant.permissions.forEach {
                if (it == "*") player.isOp = false
                attachment.setPermission(it, false)
            }

            player.removeAttachment(attachment)
        }

        player.recalculatePermissions()
    }
}