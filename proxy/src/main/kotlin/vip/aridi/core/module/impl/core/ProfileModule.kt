package vip.aridi.core.module.impl.core

import com.mongodb.client.model.Filters
import org.bson.Document
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.module.SharedManager
import vip.aridi.core.profile.Profile
import vip.aridi.core.utils.MongoUtil
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 09 - dic
 */

class ProfileModule: IModule {
    override fun order() = 3

    override fun category() = ModuleCategory.SYSTEM

    override fun load() {
        val consoleProfile = Profile(CONSOLE_ID, "Console")
        this.toSave(consoleProfile)
    }

    override fun unload() {}

    override fun reload() {}

    override fun moduleName() = "Profile"

    fun getProfile(name: String): Profile? {
        return try {
            loadProfile(name)
        } catch (_: Exception) {
            null
        }
    }

    fun getProfile(id: UUID): Profile? {
        return try {
            loadProfile(id)
        } catch (_: Exception) {
            null
        }
    }

    fun getProfiles(): List<Profile> {
        return SharedManager.databaseModule.getCollection("profiles").find().mapNotNull {
            fromDocument(it)
        }.toList()
    }

    private fun loadProfile(name: String): Profile? {
        val document = SharedManager.databaseModule.getCollection("profiles").find(Filters.eq("name", name)).first()

        return if (document == null) {
            val profile = Profile(UUID.randomUUID(), name)
            toSave(profile)
            profile
        } else {
            fromDocument(document)
        }
    }

    private fun loadProfile(id: UUID): Profile? {
        val document = SharedManager.databaseModule.getCollection("profiles").find(Filters.eq("_id", id.toString())).first()

        return if (document == null) {
            val profile = Profile(id, "???")
            toSave(profile)
            profile
        } else {
            fromDocument(document)
        }
    }

    fun calculatePermissions(permissions: ArrayList<String>, defaultPermissions: Boolean): ConcurrentHashMap<String, Boolean> {
        val toReturn = ConcurrentHashMap<String, Boolean>()

        if (defaultPermissions) {
            toReturn.putAll(SharedManager.rankModule.defaultRank.permission.associate {
                val value = !it.startsWith("-")
                return@associate (if (value) it else it.substring(1)).lowercase() to value
            })
        }

        toReturn.putAll(permissions.associate {
            val value = !it.startsWith("-")
            return@associate (if (value) it else it.substring(1)).lowercase() to value
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

    fun toSave(profile: Profile): Boolean {
        val profileDocument = Document().apply {
            this["_id"] = profile.id.toString()
            this["name"] = profile.name
            this["frozen"] = profile.frozen
            this["coins"] = profile.coins
        }

        val result = SharedManager.databaseModule.getCollection("profiles").replaceOne(
            Filters.eq("_id", profile.id.toString()),
            profileDocument,
            MongoUtil.REPLACE_OPTIONS
        )
        return result.wasAcknowledged()
    }

    val CONSOLE_ID = UUID.randomUUID()
}
