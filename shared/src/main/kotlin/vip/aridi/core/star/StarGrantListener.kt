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
        println("Grant execution")
        val uuid = UUID.fromString(data["targetId"].asString)
        println("1")
        val grantModule = SharedManager.grantModule
        println("2")
        val rankModule = SharedManager.rankModule
        println("3")

        if (grantModule == null) {
            println("debug: grantModule is null")
        }

        if (rankModule == null) {
            println("debug: rankModule is null")
        }

        println("Debug: rankModule = ${SharedManager.rankModule}")
        println("Debug: grantModule = ${SharedManager.grantModule}")


        if (!grantModule.active.containsKey(uuid)) return
        println("4")

        val grant = grantModule.gson.fromJson(data, Grant::class.java)
        println("5")
        val rank = rankModule.findById(grant.rankId) ?: return
        println("6")

        println("7")
        grantModule.findProvider().ifPresent { it.onGrantApply(uuid, grant) }
        println("8")

        if (rank.priority < grantModule.findGrantedRank(uuid).priority) return
        println("9")

        grantModule.active[uuid]?.apply {
            println("aplying grant")
            add(grant)
            grantModule.setGrant(uuid, this)
        }

        if (!grant.isPermanent()) {
            println("10")
            grantModule.active.computeIfAbsent(uuid) { ArrayList() }.add(grant)
            println("11")
        }

        println("12")
        grantModule.findProvider().ifPresent { it.onGrantChange(uuid, grant) }
        println("13")
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