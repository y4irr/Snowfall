package vip.aridi.core.rank.prompt

import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.rank.Rank
import vip.aridi.core.rank.menus.RankEditor
import vip.aridi.core.rank.menus.editor.RankMetadata
import vip.aridi.core.utils.CC
import java.lang.reflect.Field

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 24 - nov
 */

class RankModifyPrompt(
    private val rank: Rank,
    private val menu: String,
    private val toEdit: String,
    private val player: Player
):  StringPrompt() {

    override fun getPromptText(p0: ConversationContext?): String {
        return CC.translate("&ePlease type a value to modify this rank, or type &c\"cancel\" &eto cancel.")
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {
        if (input.equals("cancel", true)) {
            handleCancel(context)
            return END_OF_CONVERSATION
        }

        try {
            val field = Rank::class.java.getDeclaredField(toEdit)
            field.isAccessible = true

            when (toEdit) {
                "permissions" -> handlePermissions(input, field)
                "displayName" -> handleDisplay(input)
                "price" -> handlePrice(input, field)
                "prefix" -> handlePrefix(input)
                else -> handleGenericField(input, field)
            }

            navigateToMenu()
        } catch (e: NoSuchFieldException) {
            context.forWhom.sendRawMessage(CC.translate("&cError: Property $toEdit not found."))
        } catch (e: Exception) {
            context.forWhom.sendRawMessage(CC.translate("&cAn error occurred while modifying the rank."))
            e.printStackTrace()
        }

        return END_OF_CONVERSATION
    }

    private fun handleCancel(context: ConversationContext) {
        context.forWhom.sendRawMessage(CC.translate("&cCancelled modify rank."))
        navigateToMenu()
    }

    private fun handlePermissions(input: String, field: Field) {
        val permissionsList = field.get(rank) as MutableList<String>

        if (permissionsList.contains(input)) {
            player.sendMessage(CC.translate("&cThat rank already has this permission."))
        } else {
            permissionsList.add(input)
            field.set(rank, permissionsList)
            SharedManager.rankModule.updateRank(rank)
        }
    }

    private fun handlePrice(input: String, field: Field) {
        val priceValue = input.toIntOrNull()
        if (priceValue == null || priceValue < 0) {
            player.sendMessage(CC.translate("&cInvalid price value"))
            return
        }

        field.set(rank, priceValue)
        SharedManager.rankModule.updateRank(rank)
    }

    private fun handlePrefix(input: String) {
        rank.prefix = input
        SharedManager.rankModule.updateRank(rank)
    }

    private fun handleDisplay(input: String) {
        rank.displayName = input
        SharedManager.rankModule.updateRank(rank)
    }

    private fun handleGenericField(input: String, field: Field) {
        field.set(rank, input)
        SharedManager.rankModule.updateRank(rank)
    }

    private fun navigateToMenu() {
        if (menu == "metadata") {
            RankMetadata(rank).openMenu(player)
        } else {
            RankEditor(rank).openMenu(player)
        }
    }
}