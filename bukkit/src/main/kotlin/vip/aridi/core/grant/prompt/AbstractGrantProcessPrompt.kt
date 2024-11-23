package vip.aridi.core.grant.prompt

import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player
import vip.aridi.core.profile.Profile
import vip.aridi.core.rank.Rank
import vip.aridi.core.util.Callback
import vip.aridi.core.util.TimeUtil
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.menus.ConfirmMenu

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

abstract class AbstractGrantProcessPrompt(
    val rank: Rank,
    val profile: Profile,
    val sender: Player
) : StringPrompt() {

    enum class GrantStep {
        REASON, LENGTH
    }

    abstract fun onProcessCancelled(step: GrantStep)
    abstract fun onReasonProvided(reason: String)
    abstract fun onGrantConfirmed(duration: Long)

    private var currentStep = GrantStep.REASON
    lateinit var reason: String

    override fun getPromptText(context: ConversationContext?): String {
        return when (currentStep) {
            GrantStep.REASON -> CC.translate("&eProvide a reason for this grant, or type &ccancel &eto abort the process.")
            GrantStep.LENGTH -> CC.translate("&eEnter grant duration (\"perm\" for permanent) or type &ccancel &eto abort.")
        }
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt {
        if (input.equals("cancel", ignoreCase = true)) {
            onProcessCancelled(currentStep)
            return END_OF_CONVERSATION
        }

        return when (currentStep) {
            GrantStep.REASON -> {
                reason = input
                onReasonProvided(reason)
                currentStep = GrantStep.LENGTH
                this // Reutilizamos el mismo prompt para la siguiente fase
            }
            GrantStep.LENGTH -> {
                val duration = parseDuration(input)
                if (duration < 0) {
                    context.forWhom.sendRawMessage(CC.translate("&cInvalid duration."))
                    return END_OF_CONVERSATION
                }
                ConfirmMenu("Confirm Grant?", object : Callback<Boolean> {
                    override fun callback(confirmed: Boolean) {
                        if (confirmed) onGrantConfirmed(duration)
                        else sender.closeInventory()
                    }
                }).openMenu(sender)
                END_OF_CONVERSATION
            }
        }
    }

    private fun parseDuration(input: String): Long {
        return if (input.equals("perm", ignoreCase = true)) 0L else TimeUtil.parseTime(input)
    }
}