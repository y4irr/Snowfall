package vip.aridi.core.module.impl

import vip.aridi.core.module.IModule
import vip.aridi.core.utils.Configuration
import vip.aridi.core.Snowfall

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

class ConfigurationModule: IModule {
    lateinit var mainConfig: Configuration
    lateinit var messagesConfig: Configuration

    override fun order(): Int {
        return 1
    }

    override fun load() {
        mainConfig = Configuration(Snowfall.get(), "config", "yml")
        messagesConfig = Configuration(Snowfall.get(), "messages", "yml")
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