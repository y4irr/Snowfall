package vip.aridi.core.module.impl.system

import vip.aridi.core.module.IModule
import vip.aridi.core.profile.listener.ProfileListener
import vip.aridi.core.Snowfall
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.permissions.listener.PermissionListener

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

class ListenersModule: IModule {
    override fun order(): Int = 3

    override fun category(): ModuleCategory = ModuleCategory.SYSTEM

    override fun load() {
        ProfileListener(Snowfall.get())
        PermissionListener(Snowfall.get())
    }

    override fun unload() {

    }

    override fun reload() {

    }

    override fun moduleName(): String {
        return "Listener"
    }
}