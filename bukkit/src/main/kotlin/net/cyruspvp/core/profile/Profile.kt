package net.cyruspvp.core.profile

import com.mongodb.client.model.Filters
import net.cyruspvp.core.database.MongoDatabase
import net.cyruspvp.core.module.ModuleManager
import net.cyruspvp.core.utils.MongoUtil
import org.bson.Document
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

class Profile(
    val id: UUID,
    var name: String
) {

    var frozen = false
    var coins = 0

    fun flagsForSave(): Boolean {
        ModuleManager.profileModule.updateProfile(this)

        val profileDocument = Document()
        profileDocument["_id"] = id.toString()
        profileDocument["name"] = name
        profileDocument["frozen"] = frozen
        profileDocument["coins"] = coins

        val result = MongoDatabase.getCollection("profiles").replaceOne(
            Filters.eq("_id", this.id.toString()),
            profileDocument,
            MongoUtil.REPLACE_OPTIONS
        )
        return result.wasAcknowledged()
    }
}