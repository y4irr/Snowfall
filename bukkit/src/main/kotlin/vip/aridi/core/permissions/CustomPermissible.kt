package vip.aridi.core.permissions

import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissibleBase
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.permissions.PermissionAttachmentInfo
import vip.aridi.core.Snowfall
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import java.util.concurrent.ConcurrentHashMap


/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 19 - nov
 */

class CustomPermissible(player: Player) : PermissibleBase(player) {

    private val uuid = player.uniqueId
    val permissions = ConcurrentHashMap<String, Boolean>()

    init {
        calculatePermissions(false)
    }

    fun calculatePermissions(clear: Boolean) {
        if (clear) permissions.clear()

        val calculatedPermissions = gatherPermissions()
        calculatedPermissions.entries.forEach {
            permissions[it.key.lowercase()] = it.value
        }
    }

    override fun hasPermission(permission: Permission): Boolean {
        return hasPermission(permission.name)
    }

    override fun hasPermission(permission: String): Boolean {
        if (isOp && permissions[permission] != false) {
            return true
        }
        return permissions[permission.lowercase()] ?: false
    }

    override fun isPermissionSet(permission: Permission): Boolean {
        return isPermissionSet(permission.name)
    }

    override fun isPermissionSet(name: String): Boolean {
        return permissions.containsKey(name.lowercase())
    }

    override fun clearPermissions() {
        permissions.clear()
    }

    override fun recalculatePermissions() {
    }

    private fun gatherPermissions(): ConcurrentHashMap<String, Boolean> {

        val permissions = ArrayList<String>()

        permissions.addAll((SharedManager.grantModule.active[this.uuid] ?: ArrayList()).flatMap{it.getRank()?.permissions ?: HashSet()})
        permissions.addAll(BukkitManager.profileModule.getProfile(this.uuid)?.permissions ?: ArrayList())


        val toReturn = BukkitManager.profileModule.calculatePermissions(permissions,true)

        toReturn[Server.BROADCAST_CHANNEL_USERS] = true

        return toReturn
    }

    override fun getEffectivePermissions(): MutableSet<PermissionAttachmentInfo> {
        return permissions.entries.map {
            PermissionAttachmentInfo(this, it.key, PermissionAttachment(Snowfall.get(), this), it.value)
        }.toMutableSet()
    }
}