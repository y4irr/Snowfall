package vip.aridi.core.rank

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
    var prefix: String = "",
    var displayName: String = name,
    var suffix: String = "",
    var priority: Int = 0,
    var color: String = "GREEN",
    var defaultRank: Boolean = false,
    var permission: MutableList<String> = mutableListOf(),
    var inheritance: MutableList<String> = mutableListOf(),
    var createdAt: Long? = null,
    var hidden: Boolean = false,
    var donator: Boolean = false,
    var media: Boolean = false,
    var staff: Boolean = false,
    var discordId: String? = null,
    var price: Int = 0,
    var canBeGrantable: Boolean = true

) {

    fun save() {

    }
}


