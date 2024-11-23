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
    lateinit var mainConfig: Configuration
    lateinit var messagesConfig: Configuration

    override fun order(): Int = 1

    override fun category(): ModuleCategory = ModuleCategory.CORE

    override fun load() {
        try {
            mainConfig = Configuration(Snowfall.get(), "config")
            messagesConfig = Configuration(Snowfall.get(), "messages")
            Snowfall.get().logger.info("Loaded configuration files successfully.")
        } catch (e: Exception) {
            Snowfall.get().logger.severe("Failed to load configuration files: ${e.message}")
            e.printStackTrace()
        }
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