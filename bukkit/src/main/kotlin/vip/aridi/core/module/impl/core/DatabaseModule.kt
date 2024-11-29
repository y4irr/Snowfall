package vip.aridi.core.module.impl.core

import com.google.gson.Gson
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document
import redis.clients.jedis.JedisPool
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.utils.CC
import vip.aridi.star.RedisStarAPI
import vip.aridi.star.StarAPI

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 23 - nov
 */

class DatabaseModule : IModule {

    private lateinit var client: MongoClient
    lateinit var database: com.mongodb.client.MongoDatabase
    lateinit var jedisPool: JedisPool
    lateinit var redisAPI: StarAPI

    override fun order(): Int = 2
    override fun category(): ModuleCategory = ModuleCategory.CORE

    override fun load() {
        try {
            val configModule = ConfigurationModule()
            val uri = configModule.databaseConfig.config.getString("MONGO.URI")
                ?: throw IllegalStateException("DATABASE.URI not found in configuration")
            val dbName = configModule.databaseConfig.config.getString("MONGO.NAME")
                ?: throw IllegalStateException("DATABASE.NAME not found in configuration")

            client = MongoClients.create(uri)
            database = client.getDatabase(dbName)
            jedisPool = JedisPool(
                configModule.databaseConfig.config.getString("REDIS.IP"),
                configModule.databaseConfig.config.getInt("REDIS.PORT")
            )
            redisAPI = RedisStarAPI(
                Gson(),
                jedisPool,
                configModule.databaseConfig.config.getString("REDIS.CHANNEL"),
                configModule.databaseConfig.config.getString("REDIS.PASSWORD")
            )

            println(CC.translate("&7[&bDatabase System&7] &aSuccessfully connected to database: $dbName"))
        } catch (e: Exception) {
            throw IllegalStateException("Failed to initialize DatabaseModule: ${e.message}")
        }
    }

    override fun unload() {
        try {
            client.close()
            println(CC.translate("&7[&bDatabase System&7] &cDatabase connection closed"))
        } catch (e: Exception) {
            println(CC.translate("&7[&bDatabase System&7] &cFailed to close database connection: ${e.message}"))
        }
    }

    override fun reload() {
    }

    override fun moduleName(): String = "DatabaseModule"

    fun getCollection(collectionName: String): MongoCollection<Document> {
        if (!::database.isInitialized) {
            throw IllegalStateException("Database is not initialized yet")
        }
        return database.getCollection(collectionName)
    }

    inline fun <reified T> getCollection(collectionName: String, documentClass: Class<T>): MongoCollection<T> {
        return database.getCollection(collectionName, documentClass)
    }
}