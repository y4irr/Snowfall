package vip.aridi.core.module.impl.core

import vip.aridi.core.module.IModule
import vip.aridi.core.utils.Configuration
import vip.aridi.core.Snowfall
import vip.aridi.core.module.BukkitManager.Companion.configModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.SharedManager

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
        SharedManager.mongoUri = databaseConfig.config.getString("MONGO.URI")
        SharedManager.mongoDbName = databaseConfig.config.getString("MONGO.NAME")
        SharedManager.redisIp = databaseConfig.config.getString("REDIS.IP")
        SharedManager.redisPort = databaseConfig.config.getInt("REDIS.PORT")
        SharedManager.redisChannel = databaseConfig.config.getString("REDIS.CHANNEL")
        SharedManager.redisPassword = databaseConfig.config.getString("REDIS.PASSWORD")
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