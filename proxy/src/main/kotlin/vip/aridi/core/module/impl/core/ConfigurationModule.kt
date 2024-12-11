package vip.aridi.core.module.impl.core

import vip.aridi.core.SnowfallProxy
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.SharedManager
import vip.aridi.core.utils.Configuration

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 09 - dic
 */

class ConfigurationModule: IModule {
    var mainConfig: Configuration = Configuration(SnowfallProxy.instance, "config")
    var databaseConfig: Configuration = Configuration(SnowfallProxy.instance, "database")

    override fun order() = 1

    override fun category() = ModuleCategory.CORE

    override fun load() {
        SharedManager.mongoUri = databaseConfig.config["MONGO.URI"].toString()
        SharedManager.mongoDbName = databaseConfig.config["MONGO.NAME"].toString()
        SharedManager.redisIp = databaseConfig.config["REDIS.IP"].toString()
        SharedManager.redisPort = databaseConfig.config["REDIS.PORT"] as Int
        SharedManager.redisChannel = databaseConfig.config["REDIS.CHANNEL"].toString()
        SharedManager.redisPassword = databaseConfig.config["REDIS.PASSWORD"].toString()
    }

    override fun unload() {
        mainConfig.save()
        databaseConfig.save()
    }

    override fun reload() {
        mainConfig.reload()
        databaseConfig.reload()
    }

    override fun moduleName() = "Configuration"
}