package vip.aridi.core.grant.menu.view

import org.bukkit.ChatColor
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import vip.aridi.core.Snowfall
import vip.aridi.core.grant.Grant
import vip.aridi.core.grant.prompt.GrantRemoveReasonPrompt
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.module.impl.core.ProfileModule
import vip.aridi.core.profile.Profile
import vip.aridi.core.utils.TimeUtil
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.UnicodeUtil
import vip.aridi.core.utils.menus.button.Button
import java.util.*

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

class ViewButton(
    private val grant: Grant,
    private val target: Profile
) : Button() {

    private val rank = grant.getRank()
    private val voided = grant.isVoided()
    private val removed = grant.isRemoved()

    override fun getName(player: Player?): String {
        return CC.translate("&6${TimeUtil.formatIntoCalendarString(Date(grant.createdAt))}")
    }

    override fun getDescription(player: Player?): MutableList<String> {
        return buildList {
            add("")
            addGrantDetails()
            when {
                removed -> addRemovedDetails()
                voided -> addVoidedDetails()
                else -> add("&cClick to remove grant.")
            }
        }.let { CC.translateMutable(it.toMutableList()) }
    }

    private fun MutableList<String>.addGrantDetails() {
        val senderName = if (grant.senderId == ProfileModule.CONSOLE_ID) {
            "&r&lConsole"
        } else {
            val senderRank = SharedManager.grantModule.findGrantedRank(grant.senderId)
            "${ChatColor.valueOf(senderRank.color)}${BukkitManager.profileModule.getProfile(grant.senderId)?.name}"
        }
        add(" &f${UnicodeUtil.VERTICAL_LINE} &eBy&7: $senderName")
        add(" &f${UnicodeUtil.VERTICAL_LINE} &eRank&7: &f${rank?.displayName}")
        add(" &f${UnicodeUtil.VERTICAL_LINE} &eReason&7: &f${grant.reason}")
        if (!grant.isPermanent() && !voided) {
            add(" &f${UnicodeUtil.VERTICAL_LINE} &eDuration&7: &f${TimeUtil.formatIntoDetailedString(grant.duration)}")
        }
    }

    private fun MutableList<String>.addRemovedDetails() {
        add("")
        add("&c&lRemoved")
        add("")
        val removerProfile = BukkitManager.profileModule.getProfile(grant.removerId!!) ?: return
        add(" &f${UnicodeUtil.VERTICAL_LINE} &eBy&7: &f${removerProfile.name}")
        add(" &f${UnicodeUtil.VERTICAL_LINE} &eReason&7: &f${grant.removedReason}")
        add("")
        add("&6${TimeUtil.formatIntoCalendarString(Date(grant.removedAt!!))}")
    }

    private fun MutableList<String>.addVoidedDetails() {
        add("")
        add("&c&lExpired")
        add("")
        add("&6${TimeUtil.formatIntoCalendarString(Date(grant.createdAt + grant.duration))}")
    }

    override fun getMaterial(player: Player?): Material = Material.WOOL

    override fun getDamageValue(player: Player): Byte {
        return when {
            removed -> DyeColor.RED.woolData
            voided -> DyeColor.ORANGE.woolData
            else -> DyeColor.GREEN.woolData
        }
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType?) {
        if (grant.rankId == "Default") {
            return
        }

        val senderProfile = BukkitManager.profileModule.getProfile(player.uniqueId) ?: return

        if (removed) {
            player.sendMessage(CC.translate("&cThis rank is already removed and cannot be removed again."))
            return
        }

        if (voided) {
            if (senderProfile.root) {
                player.closeInventory()
                SharedManager.grantModule.deleteGrantById(grant.id)
                GrantsMenu(SharedManager.grantModule.findAllByPlayer(target.id), target).openMenu(player)
            }
            return
        }

        player.closeInventory()
        player.beginConversation(ConversationFactory(Snowfall.get())
            .withModality(true)
            .withPrefix(NullConversationPrefix())
            .withFirstPrompt(GrantRemoveReasonPrompt(grant, target, player))
            .withEscapeSequence("/no")
            .withLocalEcho(false)
            .withTimeout(25)
            .thatExcludesNonPlayersWithMessage("No console allowed")
            .buildConversation(player)
        )
    }


}