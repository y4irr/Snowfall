package vip.aridi.core.utils.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import vip.aridi.core.grant.Grant
import java.lang.reflect.Type
import java.util.*

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 24 - nov
 */

class GrantDeserializer : JsonDeserializer<Grant> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Grant {
        val jsonObject = json.asJsonObject

        val id = UUID.fromString(jsonObject["_id"].asString)
        val rankName = jsonObject["rankId"].asString
        val targetId = UUID.fromString(jsonObject["targetId"].asString)
        val senderId = UUID.fromString(jsonObject["senderId"].asString)
        val duration = jsonObject["duration"]?.asLong ?: 0L
        val reason = jsonObject["reason"]?.asString ?: ""

        val createdAt = extractLong(jsonObject["createdAt"]) ?: System.currentTimeMillis()
        val removerId = jsonObject["removerId"]?.asString?.let { UUID.fromString(it) }
        val removedAt = extractLong(jsonObject["removedAt"])
        val removedReason = jsonObject["removedReason"]?.asString

        return Grant(id, rankName, targetId, senderId, duration, reason).apply {
            this.createdAt = createdAt
            this.removerId = removerId
            this.removedAt = removedAt
            this.removedReason = removedReason
        }
    }

    private fun extractLong(jsonElement: JsonElement?): Long? {
        if (jsonElement == null) return null
        return when {
            jsonElement.isJsonObject && jsonElement.asJsonObject.has("\$numberLong") -> {
                jsonElement.asJsonObject["\$numberLong"].asString.toLongOrNull()
            }
            jsonElement.isJsonPrimitive -> jsonElement.asLongOrNull()
            else -> null
        }
    }

    private fun JsonElement.asLongOrNull(): Long? = try {
        this.asLong
    } catch (e: UnsupportedOperationException) {
        null
    } catch (e: NumberFormatException) {
        null
    }
}