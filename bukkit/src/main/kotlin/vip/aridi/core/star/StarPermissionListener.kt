package vip.aridi.core.star

import com.google.gson.JsonObject
import vip.aridi.core.Snowfall
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.permissions.CustomPermissible
import vip.aridi.core.profile.Profile
import vip.aridi.star.listener.StarListener
import vip.aridi.star.stellar.StellarEvent
import java.util.*

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 26 - nov
 */

class StarPermissionListener: StarListener {

    @StellarEvent(Snowfall.UPDATE_PERMISSION)
    fun updatePermissible(data: JsonObject) {
        val profile = extractProfile(data) ?: return

        val permissions = data.get("permissions")?.asString ?: return
        val remove = data.get("remove")?.asBoolean ?: false

        updateProfilePermissions(profile, permissions, remove)

        val player = Snowfall.get().server.getPlayer(profile.id) ?: return
        val permissible = ModuleManager.permissionModule.getPermissible(player) as? CustomPermissible ?: return

        updatePlayerPermissions(permissible, permissions, remove)
    }

    private fun extractProfile(data: JsonObject): Profile? {
        val profileId = data.get("_id")?.asString ?: return null
        return ModuleManager.profileModule.getProfile(UUID.fromString(profileId))
    }

    private fun updateProfilePermissions(profile: Profile, permissions: String, remove: Boolean) {
        if (remove) {
            profile.permissions.remove(permissions)
        } else {
            profile.permissions.addIfAbsent(permissions)
        }
    }

    private fun updatePlayerPermissions(permissible: CustomPermissible, permissions: String, remove: Boolean) {
        val normalizedPermission = permissions.lowercase()

        if (remove) {
            permissible.permissions.remove(normalizedPermission)
        } else {
            permissible.permissions[normalizedPermission] = !normalizedPermission.startsWith("-")
        }
    }

    private fun MutableList<String>.addIfAbsent(element: String) {
        if (!this.contains(element)) {
            this.add(element)
        }
    }
}