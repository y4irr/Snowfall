package vip.aridi.core.module.impl.system

import com.google.gson.JsonObject
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity
import org.bukkit.entity.Player
import org.bukkit.permissions.Permissible
import vip.aridi.core.Snowfall
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.permissions.CustomPermissible
import vip.aridi.core.permissions.PermissionUpdateEvent
import vip.aridi.core.permissions.listener.PermissionListener
import vip.aridi.core.profile.Profile
import java.lang.reflect.Field

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 19 - nov
 */

class PermissionModule: IModule {
    override fun order(): Int = 4

    override fun category(): ModuleCategory = ModuleCategory.SYSTEM

    override fun load() {
        PermissionListener(Snowfall.get())
        //dev.ryu.core.shared.Shared.backendManager.getJupiter().addListener(PermissionOrbitListener()) we import this to add the listener API to REDIS
    }

    override fun unload() {

    }

    override fun reload() {

    }

    override fun moduleName(): String {
        return "Permission"
    }

    private val core = Snowfall.get()
    private val server = core.server
    private val humanEntityPermissibleField: Field = CraftHumanEntity::class.java.getDeclaredField("perm").apply {
        isAccessible = true
    }

    fun update(player: Player, clear: Boolean): Boolean {
        val permissible = getPermissible(player) as? CustomPermissible ?: return false

        runAsync {
            permissible.calculatePermissions(clear)
            runSync {
                server.pluginManager.callEvent(PermissionUpdateEvent(player))
            }
        }

        return true
    }

    fun update(profile: Profile, permission: String, remove: Boolean): Boolean {
        checkThread()

        val updateSuccess = profile.flagsForSave()

        if (updateSuccess) {
            val jsonObject = JsonObject().apply {
                addProperty("_id", profile.id.toString())
                addProperty("remove", remove)
                addProperty("permission", permission)
            }
        }

        return updateSuccess
    }

    fun getPermissible(player: Player): Permissible {
        return humanEntityPermissibleField.get(player) as Permissible
    }

    fun setPermissible(player: Player, permissible: Permissible) {
        humanEntityPermissibleField.set(player, permissible)
    }

    private fun checkThread() {
        if (Bukkit.isPrimaryThread()) {
            throw IllegalStateException("Cannot update permissions on the main thread.")
        }
    }

    private fun runSync(action: () -> Unit) {
        server.scheduler.runTask(core, action)
    }

    private fun runAsync(action: () -> Unit) {
        server.scheduler.runTaskAsynchronously(core, action)
    }
}