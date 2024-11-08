package net.cyruspvp.core.module.impl

import net.cyruspvp.core.module.IModule
import net.cyruspvp.core.utils.Configuration
import net.cyruspvp.net.cyruspvp.core.Snowfall

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

    override fun order(): Int {
        return 1
    }

    override fun load() {
        mainConfig = Configuration(Snowfall.get(), "config", "yml")
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