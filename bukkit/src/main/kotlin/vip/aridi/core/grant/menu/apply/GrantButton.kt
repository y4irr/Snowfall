package vip.aridi.core.grant.menu.apply

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import vip.aridi.core.Snowfall
import vip.aridi.core.grant.prompt.GrantProcessPrompt
import vip.aridi.core.module.SharedManager
import vip.aridi.core.profile.Profile
import vip.aridi.core.rank.Rank
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.menus.button.Button

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

class GrantButton(private val rank: Rank, private val target: Profile) : Button() {

    override fun getName(player: Player?): String {
        return CC.translate(rank.displayName)
    }

    override fun getDescription(player: Player?): MutableList<String> {
        val description = mutableListOf<String>()
        description.add("")
        if (!rank.canBeGrantable) {
            description.add("&cThat rank is not grantable!")
        } else {
            description.add(styleAction(ChatColor.GREEN, "CLICK", "to give that rank to a player."))
        }
        return CC.translateMutable(description)
    }

    override fun getMaterial(player: Player?): Material {
        return Material.WOOL
    }

    override fun getDamageValue(player: Player?): Byte {
        return CC.getWoolData(rank.color)
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        if (!isRankGrantable(player)) return

        player.closeInventory()
        initiateGrantProcess(player)
    }

    /**
     * Checks if the rank can be granted, sending error messages if not.
     */
    private fun isRankGrantable(player: Player): Boolean {
        if (rank == SharedManager.rankModule.defaultRank) {
            player.sendMessage(CC.translate("&cThat rank can't be granted as it is the default rank."))
            return false
        }

        if (SharedManager.grantModule.findAllByPlayer(target.id).any { it.getRank()?.name == rank.name && !it.isRemoved() }) {
            player.sendMessage(CC.translate("&cGrant cannot be given as the player already possesses it."))
            return false
        }

        return true
    }

    /**
     * Initiates the grant process by starting a conversation with the player.
     */
    private fun initiateGrantProcess(player: Player) {
        val conversationFactory = ConversationFactory(Snowfall.get())
            .withModality(true)
            .withPrefix(NullConversationPrefix())
            .withFirstPrompt(GrantProcessPrompt(rank, target, player))
            .withEscapeSequence("/no")
            .withLocalEcho(false)
            .withTimeout(25)
            .thatExcludesNonPlayersWithMessage(CC.translate("&cOnly players can interact with this process!"))

        conversationFactory.buildConversation(player).begin()
    }
}