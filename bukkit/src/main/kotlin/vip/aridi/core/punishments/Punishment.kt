package vip.aridi.core.punishments


import java.util.UUID

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class Punishment (

    val id: String,
    val punishedUUID: UUID,
    val punisher: String,
    val type: PunishmentType,
    val reason: String,
    val modeOn: String,
    val expiresOn: String,
    val duration: String,
    val punishedIP: String,
    val ip: Boolean,
    val silent: Boolean,
    val active: Boolean

) {
    fun isPermanet(): Boolean {
        return duration.equals("Permaneted", ignoreCase = true) || expiresOn.equals("Never", ignoreCase = true)
    }
}


