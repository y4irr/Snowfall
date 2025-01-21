package vip.aridi.core.module.system

import com.google.gson.GsonBuilder
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.SharedManager
import vip.aridi.core.profile.Profile
import vip.aridi.core.punishments.*
import java.util.*
import java.util.concurrent.CompletableFuture

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 26 - dic
 */

class PunishmentModule: IModule {
    val mutes = HashMap<UUID,HashSet<PunishmentData>>()
    private val activePunishments = mutableMapOf<UUID, PunishmentData>()
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val punishmentLogs = mutableListOf<PunishmentLog>()

    fun addPunishment(
        type: PunishmentType,
        victim: UUID,
        sender: UUID,
        reason: String,
        server: String,
        duration: Long,
        silent: Boolean,
        victimDisplay: String,
        senderDisplay: String,
        ip: Boolean,
        victimProfile: Profile?
    ): Boolean {
        val updatedIps = victimProfile!!.addresses.toMutableSet()

        val punishmentData = activePunishments.values.find { it.victim == victim && it.type == type }?.apply {
            this.ipAddresses = updatedIps
        } ?: PunishmentData(UUID.randomUUID(), type, victim, sender).apply {
            this.reason = reason
            this.server = server
            this.duration = duration
            this.silent = silent
            this.ip = ip
            this.ipAddresses = updatedIps
        }

        return savePunishment(punishmentData).also { success ->
            if (success) {
                logPunishment(punishmentData, victimDisplay, senderDisplay)
            }
        }
    }

    fun pardonPunishment(
        punishmentId: UUID,
        pardoner: UUID,
        reason: String,
        silent: Boolean,
        victimDisplay: String,
        senderDisplay: String
    ): Boolean {
        val punishmentData = activePunishments[punishmentId] ?: return false

        punishmentData.apply {
            this.pardoner = pardoner
            this.pardoned = System.currentTimeMillis()
            this.pardonReason = reason
            this.pardonedSilent = silent
        }

        return savePunishment(punishmentData).also { success ->
            if (success) logPunishment(punishmentData, victimDisplay, senderDisplay, isPardon = true)
        }
    }

    fun isMuted(uuid: UUID): Boolean {
        return activePunishments.values.any {
            it.victim == uuid && it.type == PunishmentType.MUTE && it.getRemaining() > 0
        }
    }

    fun findLastPunishmentByType(uuid: UUID, punishType: PunishmentType): PunishmentData? {
        return activePunishments.values
            .filter { it.victim == uuid && it.type == punishType }
            .maxByOrNull { it.created }
    }

    fun findLastPunishment(uuid: UUID): PunishmentData? {
        return activePunishments.values
            .filter { it.victim == uuid }
            .maxByOrNull { it.created }
    }

    fun findByVictimOrIdentifier(victim: UUID, addresses: MutableList<String>): MutableSet<PunishmentData> {
        return CompletableFuture.supplyAsync {
            SharedManager.databaseModule.getCollection("punishments")
                .find(Filters.or(Filters.eq("victim", victim.toString()), Filters.or(addresses.map { Filters.eq("addresses", it) })))
                .map { SharedManager.databaseModule.gson.fromJson(it.toJson(), PunishmentData::class.java) }
                .toMutableSet()
        }.join()
    }

    fun getPunishmentIdsByType(uuid: UUID, punishType: PunishmentType): List<UUID> {
        return activePunishments.values
            .filter { it.victim == uuid && it.type == punishType }
            .map { it.id }
    }


    private fun savePunishment(punishmentData: PunishmentData): Boolean {
        activePunishments[punishmentData.id] = punishmentData
        return SharedManager.databaseModule.getCollection("punishments").updateOne(
            Filters.eq("_id", punishmentData.id.toString()),
            Document("\$set", Document.parse(gson.toJson(punishmentData))),
            UpdateOptions().upsert(true)
        ).wasAcknowledged()
    }

    fun findByIp(ipAddress: String): Set<PunishmentData> {
        return CompletableFuture.supplyAsync {
            SharedManager.databaseModule.getCollection("punishments")
                .find(Filters.eq("ipAddress", ipAddress))
                .map { gson.fromJson(it.toJson(), PunishmentData::class.java) }
                .toSet()
        }.join()
    }

    private fun logPunishment(
        punishmentData: PunishmentData,
        victimDisplay: String,
        senderDisplay: String,
        isPardon: Boolean = false
    ) {
        val log = PunishmentLog(
            punishmentData.id,
            punishmentData.type,
            punishmentData.victim,
            punishmentData.sender,
            isPardon
        ).apply {
            this.victimDisplay = victimDisplay
            this.senderDisplay = senderDisplay
            this.timestamp = System.currentTimeMillis()
        }
        punishmentLogs.add(log)
    }

    fun toJson(): String = gson.toJson(this)

    fun fromJson(json: String): PunishmentData = gson.fromJson(json, PunishmentData::class.java)
    override fun order() = 3

    override fun category() = ModuleCategory.SYSTEM

    override fun load() {
        val cursor = SharedManager.databaseModule.getCollection("punishments").find().iterator()
        cursor.use {
            while (cursor.hasNext()) {
                val punishmentData = gson.fromJson(cursor.next().toJson(), PunishmentData::class.java)
                activePunishments[punishmentData.id] = punishmentData
            }
        }
    }

    override fun unload() {
        activePunishments.clear()
    }

    override fun reload() {
        activePunishments.clear()
        val cursor = SharedManager.databaseModule.getCollection("punishments").find().iterator()
        cursor.use {
            while (cursor.hasNext()) {
                val punishmentData = gson.fromJson(cursor.next().toJson(), PunishmentData::class.java)
                activePunishments[punishmentData.id] = punishmentData
            }
        }
    }

    override fun moduleName() = "Punishment"
}