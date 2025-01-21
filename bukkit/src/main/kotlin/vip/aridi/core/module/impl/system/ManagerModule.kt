package vip.aridi.core.module.impl.system

import vip.aridi.core.Snowfall
import vip.aridi.core.grant.adapter.GrantBukkitAdapter
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.star.StarPermissionListener
import vip.aridi.core.star.StarPunishmentListener

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 19 - nov
 */

class ManagerModule: IModule {
    override fun order(): Int = 4

    override fun category(): ModuleCategory = ModuleCategory.SYSTEM

    override fun load() {
        SharedManager.grantModule.setProvider(GrantBukkitAdapter())
        Snowfall.get().server.scheduler.runTaskAsynchronously(Snowfall.get(), SharedManager.grantModule.expiryService)
        SharedManager.databaseModule.redisAPI.addListener(StarPunishmentListener())
    }

    override fun unload() {

    }

    override fun reload() {

    }

    override fun moduleName(): String {
        return "Manager"
    }
}