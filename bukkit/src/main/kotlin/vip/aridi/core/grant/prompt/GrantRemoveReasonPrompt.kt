package vip.aridi.core.grant.prompt

import org.bukkit.Bukkit
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player
import vip.aridi.core.Snowfall
import vip.aridi.core.grant.Grant
import vip.aridi.core.grant.menu.view.GrantsMenu
import vip.aridi.core.module.SharedManager
import vip.aridi.core.profile.Profile
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 24 - nov
 */

class GrantRemoveReasonPrompt(
    private val grant: Grant,
    private val target: Profile,
    private val sender: Player
): StringPrompt() {
    override fun getPromptText(p0: ConversationContext?): String {
        return CC.translate("&ePlease type a reason for this grant to be removed, or type &c\"cancel\"&e to cancel.")
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {
        if (input.equals("cancel", true)) {
            context.forWhom.sendRawMessage(CC.translate("&cCancelled removing grant."))
            return END_OF_CONVERSATION
        }

        Bukkit.getServer().scheduler.runTaskAsynchronously(Snowfall.get()) {
            if (SharedManager.grantModule.remove(grant, (context.forWhom as Player).uniqueId, input)) {
                context.forWhom.sendRawMessage(CC.translate("&aRemove grant successfully"))

                val allGrants: Set<Grant> = SharedManager.grantModule.findAllByPlayer(target.id)
                GrantsMenu(allGrants, target).openMenu(sender)

                return@runTaskAsynchronously
            }

            context.forWhom.sendRawMessage(CC.translate("&cThere was an issue removing this grant."))
        }

        return END_OF_CONVERSATION
    }


}