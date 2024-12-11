package vip.aridi.core.permission

import com.google.gson.JsonObject
import vip.aridi.core.SnowfallProxy
import vip.aridi.core.module.SharedManager
import vip.aridi.star.listener.StarListener
import vip.aridi.star.stellar.StellarEvent
import java.util.*

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 09 - dic
 */

class StarPermissionListener: StarListener {

    @StellarEvent(SharedManager.UPDATE_PERMISSION)
    fun onPermissionUpdate(data: JsonObject) {
        val player = SnowfallProxy.instance.proxy.getPlayer(UUID.fromString(data["_id"].asString)) ?: return

        val remove = data["remove"].asBoolean
        val permission = data["permission"].asString

        player.setPermission(permission,!remove)
    }
}