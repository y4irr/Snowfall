package vip.aridi.core.grant.menu.apply

import org.bukkit.entity.Player
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.profile.Profile
import vip.aridi.core.rank.Rank
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.menus.PaginatedMenu
import vip.aridi.core.utils.menus.button.Button
import vip.aridi.core.utils.menus.button.impl.GlassButton

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

class GrantMenu(
    private val profile: Profile
) : PaginatedMenu() {

    init {
        isUpdateAfterClick = true
        isAutoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player?): String {
        return CC.translate("&6&lChoose a Rank")
    }

    override fun getAllPagesButtons(player: Player?): MutableMap<Int, Button> {
        val availableRanks = getAvailableRanks(player)
        return availableRanks.mapIndexed { index, rank ->
            index to GrantButton(rank, profile)
        }.toMap().toMutableMap()
    }

    override fun getGlobalButtons(player: Player?): MutableMap<Int, Button> {
        return buildGlassButtons()
    }

    override fun size(player: Player?): Int {
        return 45
    }

    override fun getMaxItemsPerPage(player: Player?): Int {
        return 27
    }

    /**
     * Retrieves a list of ranks available to the player based on their permissions.
     */
    private fun getAvailableRanks(player: Player?): List<Rank> {
        if (player == null) return emptyList()

        val playerProfile = BukkitManager.profileModule.getProfile(player.uniqueId) ?: return emptyList()
        val grantedRank = SharedManager.grantModule.findGrantedRank(player.uniqueId) ?: return emptyList()

        return SharedManager.rankModule.findAllRanks().filter { rank ->
            rank.isEligibleForGrant(grantedRank.priority, playerProfile)
        }.sortedByDescending { it.priority }
    }

    /**
     * Builds the decorative glass buttons for the menu.
     */
    private fun buildGlassButtons(): MutableMap<Int, Button> {
        val buttons = mutableMapOf<Int, Button>()
        for (i in 0..8) {
            buttons[getSlot(i, 0)] = GlassButton(7)
            buttons[getSlot(i, 4)] = GlassButton(8)
        }
        return buttons
    }
}

/**
 * Extension function to determine if a rank is eligible for granting.
 */
private fun Rank.isEligibleForGrant(grantedRankPriority: Int, profile: Profile): Boolean {
    return when {
        profile.root -> this.priority <= grantedRankPriority
        else -> this.priority <= grantedRankPriority && !this.defaultRank && !this.hidden
    }
}