package vip.aridi.core.module.impl.system

import net.md_5.bungee.api.connection.ProxiedPlayer
import vip.aridi.core.SnowfallProxy
import vip.aridi.core.grant.GrantProxyAdapter
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.ProxyManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.permission.PermissionListener
import vip.aridi.core.permission.StarPermissionListener

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 09 - dic
 */

class PermissionModule
    : IModule {
    override fun order(): Int = 3

    override fun category(): ModuleCategory = ModuleCategory.SYSTEM

    override fun load() {
        SharedManager.databaseModule.redisAPI.addListener(StarPermissionListener())
        SnowfallProxy.instance.proxy.pluginManager.registerListener(SnowfallProxy.instance, PermissionListener())

        SharedManager.grantModule.setProvider(GrantProxyAdapter())
    }

    override fun unload() {

    }

    override fun reload() {
    }

    override fun moduleName() = "Permission"

    fun update(player: ProxiedPlayer) {
        val permissions = ArrayList<String>()

        permissions.addAll((SharedManager.grantModule.active[player.uniqueId] ?: ArrayList()).flatMap { it.getRank()?.permissions ?: HashSet() })
        permissions.addAll(ProxyManager.profileModule.getProfile(player.uniqueId)?.permissions ?: ArrayList())

        ProxyManager.profileModule.calculatePermissions(permissions, true)
    }
}