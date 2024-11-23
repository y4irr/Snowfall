package vip.aridi.core.grant.prompt

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import vip.aridi.core.Snowfall
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.profile.Profile
import vip.aridi.core.rank.Rank
import vip.aridi.core.util.TimeUtil
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

class GrantProcessPrompt(
    rank: Rank,
    profile: Profile,
    sender: Player
) : AbstractGrantProcessPrompt(rank, profile, sender) {

    override fun onProcessCancelled(step: GrantStep) {
        val message = when (step) {
            GrantStep.REASON -> "&cGrant process cancelled at the reason input stage."
            GrantStep.LENGTH -> "&cGrant process cancelled at the duration input stage."
        }
        sender.sendMessage(CC.translate(message))
    }

    override fun onReasonProvided(reason: String) {
        sender.sendMessage(CC.translate("&aReason provided: $reason. Proceeding to duration input..."))
    }

    override fun onGrantConfirmed(duration: Long) {
        Snowfall.get().server.scheduler.runTaskAsynchronously(Snowfall.get()) {
            ModuleManager.grantModule.grant(rank, profile.id, sender.uniqueId, reason, duration)

            val grantedRank = ModuleManager.grantModule.findGrantedRank(profile.id)
            val durationText = if (duration == 0L) "permanently" else "for ${TimeUtil.formatIntoDetailedString(duration)}"

            sender.sendMessage(
                CC.translate("&aSuccessfully granted ${ChatColor.valueOf(grantedRank.color)}${profile.name} ${rank.displayName} rank $durationText.")
            )
        }
    }
}