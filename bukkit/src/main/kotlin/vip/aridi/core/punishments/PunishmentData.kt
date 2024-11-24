package vip.aridi.core.punishments

import com.google.gson.annotations.SerializedName
import java.util.*

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 24 - nov
 */

data class PunishmentData(
    @SerializedName("_id") val id: UUID,
    val type: PunishmentType,
    val victim: UUID,
    val sender: UUID
) {
    var created: Long = System.currentTimeMillis()
    var server: String = "Unknown"
    var duration: Long = 0L
    var reason: String = ""
    var pardonReason: String? = null
    var silent: Boolean = true
    var pardonedSilent: Boolean = true
    var pardoned: Long? = null
    var pardoner: UUID? = null

    fun isVoided() = duration > 0 && (created + duration) <= System.currentTimeMillis()

    fun isPardoned() = pardoned != null && pardonReason != null && pardoner != null

    fun isPermanent() = duration == 0L

    fun getRemaining() = (created + duration) - System.currentTimeMillis()
}