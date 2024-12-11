package vip.aridi.core

import net.md_5.bungee.api.plugin.Plugin
import vip.aridi.core.module.ModuleLifecycleManager
import vip.aridi.core.module.ProxyManager
import vip.aridi.core.module.impl.core.ProfileModule

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
    }


    companion object {

        lateinit var instance: SnowfallProxy

    }
}