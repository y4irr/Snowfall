package vip.aridi.core.module.system

import com.google.gson.GsonBuilder
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import vip.aridi.core.grant.Grant
import vip.aridi.core.grant.service.GrantExpiryService
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.SharedManager
import vip.aridi.core.rank.Rank
import vip.aridi.core.star.StarGrantListener
import vip.aridi.core.utils.gson.GrantDeserializer
import vip.aridi.star.event.StarEvent
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 15 - nov
 */

class GrantModule: IModule {
    val grant = HashMap<UUID, Rank>()
    val active = HashMap<UUID, ArrayList<Grant>>()

    val expiryService = GrantExpiryService()
    //Expiry Service

    private var adapter: Optional<GrantAdapter> = Optional.empty()
    val gson = GsonBuilder()
        .registerTypeAdapter(Grant::class.java, GrantDeserializer())
        .create()
    private lateinit var collection: MongoCollection<Document>

    override fun order(): Int = 2

    override fun category(): ModuleCategory = ModuleCategory.SYSTEM

    override fun load() {
        collection = SharedManager.databaseModule.getCollection("grants")
        SharedManager.databaseModule.redisAPI.addListener(StarGrantListener())
    }

    override fun unload() {

    }

    override fun reload() {

    }

    override fun moduleName(): String {
        return "Grants"
    }

    fun grant(rank: Rank, target: UUID, sender: UUID, reason: String): Boolean {
        return grant(rank, target, sender, reason, 0L)
    }

    fun grant(rank: Rank, target: UUID, sender: UUID, reason: String, duration: Long): Boolean {
        val grant = Grant(UUID.randomUUID(), rank.name, target, sender, duration, reason)

        val update = update(grant)

        if (update) {
            SharedManager.databaseModule.redisAPI.sendEvent(StarEvent(SharedManager.EXECUTE_GRANT, grant))
        }

        return update
    }

    fun findGrantedRank(uuid: UUID): Rank {
        return grant[uuid] ?: SharedManager.rankModule.defaultRank
    }

    fun remove(grant: Grant, remover: UUID, reason: String): Boolean {
        grant.removerId = remover
        grant.removedAt = System.currentTimeMillis()
        grant.removedReason = reason

        val update = update(grant)

        if (update) {
            SharedManager.databaseModule.redisAPI.sendEvent(StarEvent(SharedManager.REMOVE_GRANT, grant))
        }
        return update
    }

    fun deleteGrantById(id: UUID): Boolean {
        return collection.deleteOne(Filters.eq("_id", id.toString())).wasAcknowledged()
    }

    fun findGrantById(id: UUID): Grant? {
        val document = collection.find(Filters.eq("_id", id.toString())).firstOrNull()
        return document?.let { gson.fromJson(gson.toJson(it), Grant::class.java) }
    }

    fun findGrantsByPlayer(target: UUID): Set<Grant> {
        return collection.find(Filters.eq("targetId", target.toString())).map {
            gson.fromJson(it.toJson(), Grant::class.java)
        }.toSet()
    }

    fun setGrant(uuid: UUID, grants: Collection<Grant>) {
        grant[uuid] = (grants.filter{!it.isVoided() && !it.isRemoved()}.mapNotNull {it.getRank()}.sortedBy{ it.priority }.reversed().firstOrNull() ?: SharedManager.rankModule.defaultRank)
    }

    fun findGrantsBySender(sender: UUID): Set<Grant> {
        return collection.find(Filters.eq("senderId", sender.toString())).map {
            gson.fromJson(it.toJson(), Grant::class.java)
        }.toSet()
    }

    fun findProvider(): Optional<GrantAdapter> {
        return adapter
    }

    fun update(value: Grant): Boolean {
        return SharedManager.databaseModule.getCollection("grants").updateOne(
            Filters.eq("_id", value.id.toString()),
            Document("\$set", Document.parse(gson.toJson(value))),
            UpdateOptions().upsert(true)
        ).wasAcknowledged()
    }

    fun delete(value: Grant): Boolean {
        return SharedManager.databaseModule.getCollection("grants").deleteOne(
            Filters.eq("_id", value.id.toString())
        ).wasAcknowledged()
    }
    fun findAllByPlayer(target: UUID): MutableSet<Grant> {
        return CompletableFuture.supplyAsync {
            SharedManager.databaseModule.getCollection("grants")
                .find(Filters.eq("targetId", target.toString()))
                .map { gson.fromJson(it.toJson(), Grant::class.java) }
                .toMutableSet()
        }.join()
    }

    fun setProvider(adapter: GrantAdapter?) {
        this.adapter = Optional.ofNullable(adapter)
    }

    interface GrantAdapter {
        fun onGrantApply(uuid: UUID, grant: Grant)
        fun onGrantChange(uuid: UUID, grant: Grant)
        fun onGrantExpire(uuid: UUID, grant: Grant)
        fun onGrantRemove(uuid: UUID, grant: Grant)
    }
}