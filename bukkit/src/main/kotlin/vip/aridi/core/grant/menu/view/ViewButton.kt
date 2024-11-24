package vip.aridi.core.grant.menu.view

import org.bukkit.ChatColor
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import vip.aridi.core.grant.Grant
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.module.impl.core.ProfileModule
import vip.aridi.core.profile.Profile
import vip.aridi.core.util.TimeUtil
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
            val senderRank = ModuleManager.grantModule.findGrantedRank(grant.senderId)
            "${ChatColor.valueOf(senderRank.color)}${ModuleManager.profileModule.getProfile(grant.senderId)?.name}"
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
        val removerProfile = ModuleManager.profileModule.getProfile(grant.removerId!!) ?: return
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
        if (grant.rankId == "Default") return

        val senderProfile = ModuleManager.profileModule.getProfile(player.uniqueId) ?: return

        if (voided || removed) {
            if (senderProfile.root) {
                player.closeInventory()
                ModuleManager.grantModule.deleteGrantById(grant.id)
                GrantsMenu(ModuleManager.grantModule.findAllByPlayer(target.id), target).openMenu(player)
            }
        }
    }
}