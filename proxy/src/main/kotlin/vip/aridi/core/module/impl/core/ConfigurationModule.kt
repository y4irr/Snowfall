package vip.aridi.core.module.impl.core

import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import vip.aridi.core.SnowfallProxy
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.SharedManager
import java.io.File

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 09 - dic
 */

class ConfigurationModule: IModule {
    var mainConfig: Configuration = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(File(SnowfallProxy.instance.dataFolder, "config.yml"))
    var databaseConfig: Configuration = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(File(SnowfallProxy.instance.dataFolder, "database.yml"))

    override fun order() = 1

    override fun category() = ModuleCategory.CORE

    override fun load() {
        SharedManager.mongoUri = databaseConfig["MONGO.URI"].toString()
        SharedManager.mongoDbName = databaseConfig["MONGO.NAME"].toString()
        SharedManager.redisIp = databaseConfig["REDIS.IP"].toString()
        SharedManager.redisPort = databaseConfig["REDIS.PORT"] as Int
        SharedManager.redisChannel = databaseConfig["REDIS.CHANNEL"].toString()
        SharedManager.redisPassword = databaseConfig["REDIS.PASSWORD"].toString()
        SharedManager.redisChannel = databaseConfig["REDIS.CHANNEL"].toString()
        SharedManager.redisPassword = databaseConfig["REDIS.PASSWORD"].toString()
    }

    override fun unload() {
    }

    override fun reload() {
    }

    override fun moduleName() = "Configuration"
}