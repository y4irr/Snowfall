package vip.aridi.core.module.impl.system

import vip.aridi.core.module.IModule
import vip.aridi.core.profile.listener.ProfileListener
import vip.aridi.core.Snowfall
import vip.aridi.core.module.ModuleCategory

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

class ListenersModule: IModule {
    override fun order(): Int {
        return 4
    }

    override fun category(): ModuleCategory {
        return ModuleCategory.SYSTEM
    }

    override fun load() {
        ProfileListener(Snowfall.get())
    }

    override fun unload() {

    }

    override fun reload() {

    }

    override fun moduleName(): String {
        return "Listener"
    }
}