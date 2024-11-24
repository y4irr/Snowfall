package vip.aridi.core.utils.gson

import com.google.gson.*
import java.lang.reflect.Type
import vip.aridi.core.rank.Rank

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 23 - nov
 */

class RankDeserializer : JsonDeserializer<Rank> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Rank {
        val jsonObject = json.asJsonObject
        val rank = Rank(jsonObject["name"].asString)

        rank.createdAt = when {
            jsonObject["createdAt"].isJsonObject && jsonObject["createdAt"].asJsonObject.has("\$numberLong") -> {
                jsonObject["createdAt"].asJsonObject["\$numberLong"].asLong
            }
            jsonObject["createdAt"].isJsonPrimitive -> jsonObject["createdAt"].asLong
            else -> System.currentTimeMillis()
        }

        rank.prefix = jsonObject["prefix"]?.asString ?: ""
        rank.displayName = jsonObject["displayName"]?.asString ?: rank.name
        rank.suffix = jsonObject["suffix"]?.asString ?: ""
        rank.priority = jsonObject["priority"]?.asInt ?: 0
        rank.color = jsonObject["color"]?.asString ?: "GREEN"
        rank.defaultRank = jsonObject["defaultRank"]?.asBoolean ?: false
        rank.permission = jsonObject["permission"]?.asJsonArray?.map { it.asString }?.toMutableList() ?: mutableListOf()
        rank.inheritance = jsonObject["inheritance"]?.asJsonArray?.map { it.asString }?.toMutableList() ?: mutableListOf()
        rank.hidden = jsonObject["hidden"]?.asBoolean ?: false
        rank.staff = jsonObject["staff"]?.asBoolean ?: false
        rank.discordId = jsonObject["discordId"]?.asString
        rank.price = jsonObject["price"]?.asInt ?: 0
        rank.canBeGrantable = jsonObject["canBeGrantable"]?.asBoolean ?: true

        return rank
    }
}