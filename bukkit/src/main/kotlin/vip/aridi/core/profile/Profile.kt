package vip.aridi.core.profile

import com.mongodb.client.model.Filters
import vip.aridi.core.database.MongoDatabase
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.util.MongoUtil
import org.bson.Document
import java.util.*

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
    var permissions: MutableList<String> = mutableListOf()

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