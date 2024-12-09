package vip.aridi.core.grant.adapter

import vip.aridi.core.module.system.GrantModule
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import vip.aridi.core.Snowfall
import vip.aridi.core.grant.Grant
import vip.aridi.core.grant.event.GrantApplyEvent
import vip.aridi.core.grant.event.GrantExpireEvent
import vip.aridi.core.grant.event.GrantRemoveEvent
import vip.aridi.core.module.BukkitManager
import java.util.*

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 19 - nov
 */

class GrantBukkitAdapter : GrantModule.GrantAdapter {

    private val core = Snowfall.get()
    private val server = core.server

    override fun onGrantApply(uuid: UUID, grant: Grant) {
        getPlayer(uuid)?.let { player ->
            BukkitManager.permissionModule.update(player, true)
            runEvent {
                println("Grant applied")
                server.pluginManager.callEvent(GrantApplyEvent(player, grant))
            }
        }
    }

    override fun onGrantChange(uuid: UUID, grant: Grant) {

    }

    override fun onGrantExpire(uuid: UUID, grant: Grant) {
        getPlayer(uuid)?.let { player ->
            grant.getRank()?.takeIf { it.hidden }?.let { rank ->
                player.sendMessage("${ChatColor.LIGHT_PURPLE}Your ${rank.displayName}${ChatColor.LIGHT_PURPLE} rank has expired.")
            }
            BukkitManager.permissionModule.update(player, true)
            runEvent { server.pluginManager.callEvent(GrantExpireEvent(player, grant)) }
        }
    }

    override fun onGrantRemove(uuid: UUID, grant: Grant) {
        getPlayer(uuid)?.let { player ->
            BukkitManager.permissionModule.update(player, true)
            runEvent { server.pluginManager.callEvent(GrantRemoveEvent(player, grant)) }
        }
    }

    private fun getPlayer(uuid: UUID): Player? {
        return server.getPlayer(uuid)
    }

    private fun runEvent(action: () -> Unit) {
        server.scheduler.runTask(core, action)
    }
}