package vip.aridi.core.module.impl.core

import com.mongodb.client.model.Filters
import vip.aridi.core.module.IModule
import vip.aridi.core.profile.Profile
import org.bson.Document
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.ModuleManager
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

class ProfileModule : IModule {

    private var profiles = mutableMapOf<UUID, Profile>()
    private var profilesByName = mutableMapOf<String, Profile>()

    override fun order(): Int {
        return 3
    }

    override fun category(): ModuleCategory = ModuleCategory.CORE

    override fun load() {
        val consoleProfile = Profile(CONSOLE_ID, "Console")
        this.updateProfile(consoleProfile)
    }

    override fun unload() {

    }

    override fun reload() {}

    override fun moduleName(): String {
        return "Profile"
    }

    fun getProfile(name: String): Profile? {
        val profile = this.profilesByName[name]
        try {
            if (profile == null) {
                return this.loadProfile(name)
            }
        } catch (_: Exception) {
            return null
        }
        return profile
    }

    fun getProfile(sender: CommandSender): Profile? {
        return this.getProfile(sender.name)
    }

    fun getProfile(player: Player): Profile? {
        return this.getProfile(player.uniqueId)
    }

    fun getProfile(id: UUID): Profile? {
        val profile = this.profiles[id]
        try {
            if (profile == null) {
                return this.loadProfile(id)
            }
        } catch (_: Exception) {
            return null
        }
        return profile
    }

    fun getProfiles(): List<Profile> {
        return ModuleManager.databaseModule.getCollection("profiles").find().mapNotNull {
            fromDocument(it)
        }.toList()
    }

    fun loadProfile(name: String): Profile? {
        val document = ModuleManager.databaseModule.getCollection("profiles").find(Filters.eq("name", name)).first()

        if (document == null) {
            val profile = Profile(Bukkit.getOfflinePlayer(name).uniqueId, name)
            profile.flagsForSave()
            return profile
        }

        return fromDocument(document)
    }

    fun loadProfile(id: UUID): Profile? {
        val document = ModuleManager.databaseModule.getCollection("profiles").find(Filters.eq("_id", id.toString())).first()

        if (document == null) {
            val profile = Profile(id, Bukkit.getOfflinePlayer(id).name ?: "Unknown")
            profile.flagsForSave()
            return profile
        }

        return fromDocument(document)
    }

    fun updateProfile(profile: Profile) {
        this.profiles[profile.id] = profile
        this.profilesByName[profile.name] = profile
    }

    fun deleteProfile(id: UUID): Profile? {
        return this.profiles.remove(id)
    }

    fun calculatePermissions(permissions: ArrayList<String>,defaultPermissions: Boolean): ConcurrentHashMap<String, Boolean> {

        val toReturn = ConcurrentHashMap<String,Boolean>()

        if (defaultPermissions) {
            toReturn.putAll(ModuleManager.rankModule.defaultRank.permission.associate{
                val value = !it.startsWith("-")
                return@associate (if (value) it else it.substring(1)).toLowerCase() to value
            })
        }

        toReturn.putAll(permissions.associate{
            val value = !it.startsWith("-")
            return@associate (if (value) it else it.substring(1)).toLowerCase() to value
        })

        return toReturn
    }

    private fun fromDocument(document: Document): Profile? {
        return try {
            val id = UUID.fromString(document.getString("_id"))
            val name = document.getString("name")
            val frozen = document.getBoolean("frozen", false)
            val coins = document.getInteger("coins", 0)

            Profile(id, name).apply {
                this.frozen = frozen
                this.coins = coins
            }
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        val CONSOLE_ID = UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670")

        val NULL_PROFILE = "${ChatColor.RED}Your profile hasn't loaded, please re-join or contact an administrator."
    }
}
