package vip.aridi.core.module.impl.core

import vip.aridi.core.module.IModule
import vip.aridi.core.utils.Configuration
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

class ConfigurationModule: IModule {
    var mainConfig: Configuration = Configuration(Snowfall.get(), "config")
    var messagesConfig: Configuration = Configuration(Snowfall.get(), "messages")
    var databaseConfig: Configuration = Configuration(Snowfall.get(), "database")

    override fun order(): Int = 1

    override fun category(): ModuleCategory = ModuleCategory.CORE

    override fun load() {
    }

    override fun unload() {
        mainConfig.save()
    }

    override fun reload() {
        mainConfig.reload()
    }

    override fun moduleName(): String {
        return "Configuration"
    }
}