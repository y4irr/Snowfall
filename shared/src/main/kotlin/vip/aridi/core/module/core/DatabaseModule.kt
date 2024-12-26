package vip.aridi.core.module.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document
import redis.clients.jedis.JedisPool
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.SharedManager.mongoDbName
import vip.aridi.core.module.SharedManager.mongoUri
import vip.aridi.core.module.SharedManager.redisChannel
import vip.aridi.core.module.SharedManager.redisIp
import vip.aridi.core.module.SharedManager.redisPassword
import vip.aridi.core.module.SharedManager.redisPort
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

class DatabaseModule: IModule {

    private lateinit var client: MongoClient
    val gson: Gson = GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create()
    lateinit var database: com.mongodb.client.MongoDatabase
    lateinit var jedisPool: JedisPool
    lateinit var redisAPI: StarAPI

    override fun order(): Int = 2
    override fun category(): ModuleCategory = ModuleCategory.CORE

    override fun load() {
        try {
            client = MongoClients.create(mongoUri)
            database = client.getDatabase(mongoDbName)
            jedisPool = JedisPool(
                redisIp,
                redisPort
            )
            redisAPI = RedisStarAPI(
                gson,
                jedisPool,
                redisChannel,
                redisPassword
            )

        } catch (e: Exception) {
            throw IllegalStateException("Failed to initialize DatabaseModule: ${e.message}")
        }
    }

    override fun unload() {
        try {
            client.close()
        } catch (e: Exception) {
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