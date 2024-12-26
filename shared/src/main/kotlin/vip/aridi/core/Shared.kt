package vip.aridi.core

import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleLifecycleManager
import vip.aridi.core.module.system.GrantModule
import vip.aridi.core.module.system.RankModule

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 08 - dic
 */

class Shared: ModuleLifecycleManager {

    companion object {
        @JvmStatic
        val rankModule = RankModule()
        val grantModule = GrantModule()
    }
    override fun startup() {
        TODO("Not yet implemented")
    }

    override fun enabled() {
        TODO("Not yet implemented")
    }

    override fun disabled() {
        TODO("Not yet implemented")
    }

    override fun loadModules() {
        TODO("Not yet implemented")
    }

    override fun unloadModules() {
        TODO("Not yet implemented")
    }

    override fun addModules(iModule: IModule) {
        TODO("Not yet implemented")
    }

    override fun reloadModule(moduleName: Int) {
        TODO("Not yet implemented")
    }
}