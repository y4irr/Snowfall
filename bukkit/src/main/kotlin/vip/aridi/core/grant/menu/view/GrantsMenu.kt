package vip.aridi.core.grant.menu.view

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import vip.aridi.core.grant.Grant
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.module.impl.system.RankModule
import vip.aridi.core.profile.Profile
import vip.aridi.core.util.TimeUtil
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.UnicodeUtil
import vip.aridi.core.utils.menus.PaginatedMenu
import vip.aridi.core.utils.menus.button.Button
import vip.aridi.core.utils.menus.button.impl.GlassButton
import vip.aridi.core.utils.menus.button.impl.MenuButton

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

class GrantsMenu(
    private val grants: Set<Grant>,
    private val profile: Profile
) : PaginatedMenu() {

    override fun getPrePaginatedTitle(player: Player?): String {
        val rank = ModuleManager.grantModule.findGrantedRank(profile.id)
        return CC.translate("${rank.color}${profile.name}&7's grants")
    }

    override fun getAllPagesButtons(player: Player): MutableMap<Int, Button> {
        return grants.filter { player.hasPermission("snowfall.grant.${it.rankId}") }
            .sortedByDescending { it.createdAt }
            .mapIndexed { index, grant -> index to ViewButton(grant, profile) }
            .toMap().toMutableMap()
    }

    override fun getGlobalButtons(player: Player?): MutableMap<Int, Button> {
        return mutableMapOf<Int, Button>().apply {
            for (i in 0..8) {
                this[getSlot(i, 0)] = GlassButton(7)
                this[getSlot(i, 4)] = GlassButton(7)
            }

            val rank = ModuleManager.grantModule.findGrantedRank(profile.id)
            val currentGrant = grants.maxByOrNull { it.getRank()?.priority ?: 0 } ?: return@apply
            this[4] = createProfileButton(rank.color, rank.displayName, currentGrant)
        }
    }

    private fun createProfileButton(rankColor: String, rankDisplayName: String, grant: Grant): MenuButton {
        return MenuButton()
            .name("${ChatColor.valueOf(rankColor)}${profile.name}")
            .playerTexture(profile.name)
            .lore(
                listOf(
                    "&6&lProfile",
                    " &f${UnicodeUtil.VERTICAL_LINE} &eId&7: &f${profile.id.toString().replace("-", "")}",
                    "",
                    "&6&lRank",
                    " &f${UnicodeUtil.VERTICAL_LINE} &eName&7: &f$rankDisplayName",
                    " &f${UnicodeUtil.VERTICAL_LINE} &eExpires&7: &f${if (grant.isPermanent()) "Never" else TimeUtil.formatIntoDetailedString(grant.duration)}"
                )
            )
    }

    override fun size(player: Player?): Int = 45

    override fun getMaxItemsPerPage(player: Player?): Int = 27
}