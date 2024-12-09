package vip.aridi.core.module.system

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.SharedManager
import vip.aridi.core.rank.Rank
import vip.aridi.core.star.StarRankListener
import vip.aridi.core.utils.gson.RankDeserializer
import java.util.concurrent.CompletableFuture

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 14 - nov
 */

class RankModule: IModule {

    val gson = GsonBuilder()
        .registerTypeAdapter(Rank::class.java, RankDeserializer())
        .create()


    fun getAllRanks(): MutableSet<Rank> {
        val rankSet = mutableSetOf<Rank>()
        SharedManager.databaseModule.getCollection("ranks").find().forEach { document ->
            val rank = gson.fromJson(document.toJson(), Rank::class.java)
            rankSet.add(rank)
        }
        return rankSet
    }

    fun getRankById(id: String): Rank? {
        val document = SharedManager.databaseModule.getCollection("ranks").find(Document("_id", id)).firstOrNull() ?: return null
        return gson.fromJson(document.toJson(), Rank::class.java)
    }

    fun findById(id: String): Rank? {
        return cache[id]
    }
    fun updateRank(rank: Rank): Boolean {
        val updateResult = SharedManager.databaseModule.getCollection("ranks").updateOne(
            Filters.eq("_id", rank.name),
            Document("\$set", Document.parse(gson.toJson(rank))),
            UpdateOptions().upsert(true))
        return updateResult.wasAcknowledged()
    }

    val cache = hashMapOf<String, Rank>()

    lateinit var defaultRank: Rank

    override fun order(): Int = 1

    override fun category(): ModuleCategory = ModuleCategory.SYSTEM

    override fun load() {
        cache.putAll(loadRanks())
        defaultRank = loadDefaultRank()
        SharedManager.databaseModule.redisAPI.addListener(StarRankListener())
    }

    override fun unload() {
        cache.clear()

    }

    override fun reload() {
        cache.clear()
        cache.putAll(loadRanks())
        defaultRank = loadDefaultRank()
    }


    private fun loadRanks(): Map<String, Rank> {
        return SharedManager.databaseModule.getCollection("ranks").find()
            .map { gson.fromJson(it.toJson(), Rank::class.java) }
            .associateBy { it.name }
            .toMutableMap()
    }

    private fun loadDefaultRank(): Rank {
        val defaultRankId = "Default"
        if (cache.containsKey(defaultRankId)) {
            return cache[defaultRankId]!!
        }

        val newDefaultRank = Rank(defaultRankId).apply {
            defaultRank = true
            displayName = "&aDefault"
            prefix = "&a"
            createdAt = System.currentTimeMillis()
        }

        if (updateRank(newDefaultRank)) {
            cache[newDefaultRank.name] = newDefaultRank
        }
        return newDefaultRank
    }

    fun findAllRanks(): MutableSet<Rank> {
        return CompletableFuture.supplyAsync {
            val cursor = SharedManager.databaseModule.getCollection("ranks").find()
            val rankSet = mutableSetOf<Rank>()
            cursor.forEach {
                try {
                    val rank = gson.fromJson(it.toJson(), Rank::class.java)
                    rankSet.add(rank)
                } catch (e: JsonSyntaxException) {
                    println("Error deserializing Rank: ${e.message}")
                }
            }
            rankSet
        }.join()
    }

    fun deleteRank(id: String): Boolean {
        val deleteResult = SharedManager.databaseModule.getCollection("ranks").deleteOne(Filters.eq("_id", id))
        if (deleteResult.wasAcknowledged()) {
            cache.remove(id)
        }
        return deleteResult.wasAcknowledged()
    }

    override fun moduleName(): String {
        return "Rank"
    }
}