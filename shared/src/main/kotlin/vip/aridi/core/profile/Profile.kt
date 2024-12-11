package vip.aridi.core.profile

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

    var chatColor: String = "WHITE"

    var currentServer: String? = null
    var lastServer: String? = null

    var frozen = false

    var coins = 0
    var discordId: String? = null

    var root = false
    var online: Boolean = false

    var address: String? = null
    var addresses: MutableList<String> = mutableListOf()

    var firstJoined: Long = System.currentTimeMillis()
    var lastJoined: Long? = null

    var permissions: MutableList<String> = mutableListOf()
}