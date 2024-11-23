package vip.aridi.core.module.impl.system

import com.google.gson.Gson
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document
import vip.aridi.core.database.MongoDatabase
import vip.aridi.core.grant.Grant
import vip.aridi.core.grant.service.GrantExpiryService
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.rank.Rank
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
    private val gson = Gson()
    private lateinit var collection: MongoCollection<Document>

    override fun order(): Int = 2

    override fun category(): ModuleCategory = ModuleCategory.SYSTEM

    override fun load() {
        collection = MongoDatabase.getCollection("grants")
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
        return false
    }

    fun findGrantedRank(uuid: UUID): Rank {
        return grant[uuid] ?: ModuleManager.rankModule.defaultRank
    }

    fun remove(grant: Grant, remover: UUID, reason: String): Boolean {
        grant.removerId = remover
        grant.removedAt = System.currentTimeMillis()
        grant.removedReason = reason

        return false
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
        grant[uuid] = (grants.filter{!it.isVoided() && !it.isRemoved()}.mapNotNull {it.getRank()}.sortedBy{it.priority}.reversed().firstOrNull() ?: ModuleManager.rankModule.defaultRank)
    }

    fun findGrantsBySender(sender: UUID): Set<Grant> {
        return collection.find(Filters.eq("senderId", sender.toString())).map {
            gson.fromJson(it.toJson(), Grant::class.java)
        }.toSet()
    }

    fun findProvider(): Optional<GrantAdapter> {
        return adapter
    }

    fun findAllByPlayer(target: UUID): MutableSet<Grant> {
        return CompletableFuture.supplyAsync {
            MongoDatabase.getCollection("grants")
                .find(Filters.eq("target", target.toString()))
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