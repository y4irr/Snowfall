package vip.aridi.core.module.impl.system

import vip.aridi.core.Snowfall
import vip.aridi.core.grant.adapter.GrantBukkitAdapter
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.ModuleManager

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 19 - nov
 */

class ManagerModule: IModule {
    override fun order(): Int = 6

    override fun category(): ModuleCategory = ModuleCategory.SYSTEM

    override fun load() {
        ModuleManager.grantModule.setProvider(GrantBukkitAdapter())
        Snowfall.get().server.scheduler.runTaskAsynchronously(Snowfall.get(), ModuleManager.grantModule.expiryService)
    }

    override fun unload() {

    }

    override fun reload() {

    }

    override fun moduleName(): String {
        return "Manager"
    }
}