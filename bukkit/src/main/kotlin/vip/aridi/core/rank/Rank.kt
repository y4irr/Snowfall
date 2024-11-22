package vip.aridi.core.rank

import com.avaje.ebeaninternal.server.lib.sql.Prefix
import com.avaje.ebeaninternal.server.subclass.GenSuffix
import org.apache.logging.log4j.core.net.Priority
import vip.aridi.core.Snowfall
import vip.aridi.core.module.ModuleManager
import javax.persistence.Inheritance

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */
 
class Rank (
    var name: String,
    var prefix: String? = null,
    var displayName: String = name,
    var suffix: String? = null,
    var priority: Int = 0,
    var color: String? = null,
    var defaultRank: Boolean = false,
    var permission: MutableList<String> = mutableListOf(),
    var inheritance: MutableList<String> = mutableListOf(),
    var createdAt: Long? = null,
    var hidden: Boolean = false,
    var staff: Boolean = false,
    var discordId: String? = null,
    var canBeGrantable: Boolean = true

) {

    fun save() {

    }
}


