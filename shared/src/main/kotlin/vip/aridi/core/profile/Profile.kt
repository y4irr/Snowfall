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

    var frozen = false
    var coins = 0
    var root = false
    var permissions: MutableList<String> = mutableListOf()
}