package vip.aridi.core.module.impl.system

import vip.aridi.core.SnowfallProxy
import vip.aridi.core.listener.ProxyListener
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 09 - dic
 */

class ListenerModule: IModule {
    private val instance = SnowfallProxy.instance

    override fun order() = 4

    override fun category() = ModuleCategory.SYSTEM

    override fun load() {
        instance.proxy.pluginManager.registerListener(instance, ProxyListener(instance))

    }

    override fun unload() {
        TODO("Not yet implemented")
    }

    override fun reload() {
        TODO("Not yet implemented")
    }

    override fun moduleName() = "Listener"
}