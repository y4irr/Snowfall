package net.cyruspvp.core.database

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import net.cyruspvp.core.module.ModuleManager
import org.bson.Document

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

object MongoDatabase {
    private val client = MongoClients.create(ModuleManager.configModule.mainConfig.config.getString("DATABASE.URI"))
    val database = client.getDatabase(ModuleManager.configModule.mainConfig.config.getString("DATABASE.NAME"))

    fun getCollection(collectionName: String): MongoCollection<Document> {
        return database.getCollection(collectionName)
    }

    inline fun <reified T> getCollection(collectionName: String, documentClass: Class<T>): MongoCollection<T> {
        return database.getCollection(collectionName, documentClass)
    }

    fun close() {
        client.close()
    }
}