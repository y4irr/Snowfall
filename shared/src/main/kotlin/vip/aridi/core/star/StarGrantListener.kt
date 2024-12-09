package vip.aridi.core.star

import com.google.gson.JsonObject
import vip.aridi.core.grant.Grant
import vip.aridi.core.module.SharedManager
import vip.aridi.star.listener.StarListener
import vip.aridi.star.stellar.StellarEvent
import java.util.*
import kotlin.collections.ArrayList

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 28 - nov
 */

class StarGrantListener : StarListener {

    @StellarEvent(SharedManager.EXECUTE_GRANT)
    fun onGrantExecution(data: JsonObject) {
        val uuid = UUID.fromString(data["targetId"].asString)
        val grantModule = SharedManager.grantModule
        val rankModule = SharedManager.rankModule

        if (!grantModule.active.containsKey(uuid)) return

        val grant = grantModule.gson.fromJson(data, Grant::class.java)
        val rank = rankModule.findById(grant.rankId) ?: return

        grantModule.findProvider().ifPresent { it.onGrantApply(uuid, grant) }

        if (rank.priority < grantModule.findGrantedRank(uuid).priority) return

        grantModule.active[uuid]?.apply {
            add(grant)
            grantModule.setGrant(uuid, this)
        }

        if (!grant.isPermanent()) {
            grantModule.active.computeIfAbsent(uuid) { ArrayList() }.add(grant)
        }

        grantModule.findProvider().ifPresent { it.onGrantChange(uuid, grant) }
    }

    @StellarEvent(SharedManager.REMOVE_GRANT)
    fun onGrantRemove(data: JsonObject) {
        val uuid = UUID.fromString(data["targetId"].asString)
        val grantModule = SharedManager.grantModule

        if (!grantModule.active.containsKey(uuid)) return

        val grant = grantModule.gson.fromJson(data, Grant::class.java)

        grantModule.active[uuid]?.apply {
            removeIf { it.rankId == grant.rankId }
            add(grant)
            grantModule.setGrant(uuid, this)
        }

        grantModule.findProvider().ifPresent { it.onGrantRemove(uuid, grant) }
    }
}