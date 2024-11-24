package vip.aridi.core.punishments

import com.google.gson.GsonBuilder
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import vip.aridi.core.grant.Grant
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.utils.gson.GrantDeserializer
import java.util.UUID

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 24 - nov
 */

class Punishment {
    private val activePunishments = mutableMapOf<UUID, PunishmentData>()
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val punishmentLogs = mutableListOf<PunishmentLog>()
    private val collection = ModuleManager.databaseModule.getCollection("punishments")

    fun loadPunishments() {
        val cursor = collection.find().iterator()
        cursor.use {
            while (cursor.hasNext()) {
                val punishmentData = gson.fromJson(cursor.next().toJson(), PunishmentData::class.java)
                activePunishments[punishmentData.id] = punishmentData
            }
        }
    }

    fun reloadPunishments() {
        activePunishments.clear()
        loadPunishments()
    }

    fun addPunishment(
        type: PunishmentType,
        victim: UUID,
        sender: UUID,
        reason: PunishmentReason,
        server: String,
        duration: Long,
        silent: Boolean,
        victimDisplay: String,
        senderDisplay: String
    ): Boolean {
        val punishmentData = PunishmentData(UUID.randomUUID(), type, victim, sender).apply {
            this.reason = reason.message
            this.server = server
            this.duration = duration
            this.silent = silent
        }

        return savePunishment(punishmentData).also { success ->
            if (success) logPunishment(punishmentData, victimDisplay, senderDisplay)
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
    private fun savePunishment(punishmentData: PunishmentData): Boolean {
        activePunishments[punishmentData.id] = punishmentData
        return collection.updateOne(
            Filters.eq("_id", punishmentData.id.toString()),
            Document("\$set", Document.parse(gson.toJson(punishmentData))),
            UpdateOptions().upsert(true)
        ).wasAcknowledged()
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

    fun fromJson(json: String): Punishment = gson.fromJson(json, Punishment::class.java)
}