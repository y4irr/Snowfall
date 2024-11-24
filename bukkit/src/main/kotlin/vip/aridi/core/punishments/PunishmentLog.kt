package vip.aridi.core.punishments

import java.util.*

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 24 - nov
 */

data class PunishmentLog(
    val punishmentId: UUID,
    val type: PunishmentType,
    val victim: UUID,
    val sender: UUID,
    val isPardon: Boolean
) {
    var timestamp: Long = System.currentTimeMillis()
    var victimDisplay: String = "Unknown"
    var senderDisplay: String = "Unknown"
}