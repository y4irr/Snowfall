package vip.aridi.core.star

import com.google.gson.JsonObject
import vip.aridi.core.module.SharedManager
import vip.aridi.core.rank.Rank
import vip.aridi.star.listener.StarListener
import vip.aridi.star.stellar.StellarEvent

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 26 - nov
 */

class StarRankListener : StarListener {

    @StellarEvent(SharedManager.CREATE_RANK)
    fun onRankCreation(data: JsonObject) {
        updateRank(data, createIfAbsent = true)
    }

    @StellarEvent(SharedManager.DELETE_RANK)
    fun onRankDeletion(data: JsonObject) {
        SharedManager.rankModule.cache.remove(data["_id"].asString)
    }

    @StellarEvent(SharedManager.UPDATE_RANK)
    fun onRankUpdate(data: JsonObject) {
        updateRank(data, createIfAbsent = false)
    }

    private fun updateRank(data: JsonObject, createIfAbsent: Boolean) {
        val rankModule = SharedManager.rankModule
        val rankId = data["_id"].asString
        val existingRank = rankModule.findById(rankId)

        if (existingRank == null && createIfAbsent) {
            rankModule.cache[rankId] = rankModule.gson.fromJson(data, Rank::class.java)
        } else {
            rankModule.cache.replace(rankId, rankModule.gson.fromJson(data, Rank::class.java))
        }
    }
}