package vip.aridi.core.rank.prompt

import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.rank.Rank
import vip.aridi.core.rank.menus.RankMenu
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 24 - nov
 */

class RankCreatePrompt(
    private val player: Player
): StringPrompt() {
    override fun getPromptText(p0: ConversationContext?): String {
        return CC.translate("&ePlease type a name for this rank to be created, or type &c\"cancel\" &eto cancel.")
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {
        if (input.equals("cancel", true)) {
            handleCancel(context)
            return null
        }

        try {
            val existingRank = SharedManager.rankModule.findById(input)
            if (existingRank != null) {
                context.forWhom.sendRawMessage(CC.translate("&cThere's an existing rank with that name"))
            } else {
                createNewRank(input)
                context.forWhom.sendRawMessage(CC.translate("&aRank &f$input &ahas been created successfully."))
            }
        } catch (ex: Exception) {
            context.forWhom.sendRawMessage(CC.translate("&cThere was an issue creating this rank."))
        }

        RankMenu().openMenu(player)
        return null
    }

    private fun handleCancel(context: ConversationContext) {
        context.forWhom.sendRawMessage(CC.translate("&cCancelled creating rank."))
        RankMenu().openMenu(player)
    }

    private fun createNewRank(input: String) {
        val rank = Rank(input).apply {
            displayName = "&7$name"
            createdAt = System.currentTimeMillis()
        }

        SharedManager.rankModule.updateRank(rank)
        //Redis packet here.
    }
}