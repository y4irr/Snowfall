package vip.aridi.core

import net.md_5.bungee.api.plugin.Plugin
import vip.aridi.core.grant.GrantProxyAdapter
import vip.aridi.core.module.ModuleLifecycleManager
import vip.aridi.core.module.ProxyManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.module.impl.core.ProfileModule
import java.util.concurrent.TimeUnit

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 08 - dic
 */

class SnowfallProxy: Plugin() {
    private lateinit var moduleLifecycleManager: ModuleLifecycleManager


    override fun onEnable() {
        instance = this

        moduleLifecycleManager = ProxyManager()

        SharedManager.grantModule.setProvider(GrantProxyAdapter())
        proxy.scheduler.schedule(this, SharedManager.grantModule.expiryService, 2L, 2L, TimeUnit.SECONDS)
    }


    companion object {

        lateinit var instance: SnowfallProxy

    }
}