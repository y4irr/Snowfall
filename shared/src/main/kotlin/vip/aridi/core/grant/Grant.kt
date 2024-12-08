package vip.aridi.core.grant

import com.google.gson.annotations.SerializedName
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.rank.Rank
import java.util.UUID

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 14 - nov
 */

class Grant(
    @SerializedName("_id") val id: UUID,
    val rankId: String,
    val targetId: UUID,
    val senderId: UUID,
    var duration: Long = 0L,
    var reason: String = "",
    var createdAt: Long = System.currentTimeMillis(),
    var removedAt: Long? = null,
    var removedReason: String? = null,
    var removerId: UUID? = null
) {

    fun isActive(): Boolean = !isVoided() && !isRemoved()

    fun isVoided(): Boolean = duration > 0 && getRemainingTime() <= 0

    fun isRemoved(): Boolean = removedAt != null && removedReason != null && removerId != null

    fun getExpirationTime(): Long = createdAt + duration

    fun getRemainingTime(): Long = if (isVoided()) 0 else getExpirationTime() - System.currentTimeMillis()

    fun isPermanent(): Boolean = duration == 0L

    fun getRank(): Rank? = ModuleManager.rankModule.findById(rankId)

    fun getRankPriority(): Int = getRank()?.priority ?: 0

    companion object {
        const val GRANT_REMOVE = "GRANT_REMOVE"
        const val GRANT_EXECUTE = "GRANT_EXECUTE"
    }
}