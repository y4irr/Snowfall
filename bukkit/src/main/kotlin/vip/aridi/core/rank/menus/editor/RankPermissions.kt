package vip.aridi.core.rank.menus.editor

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import vip.aridi.core.Snowfall
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.rank.Rank
import vip.aridi.core.rank.menus.RankEditor
import vip.aridi.core.rank.prompt.RankModifyPrompt
import vip.aridi.core.utils.UnicodeUtil
import vip.aridi.core.utils.menus.Menu
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
 * Date: 23 - nov
 */

class RankPermissions(
    private val rank: Rank
): PaginatedMenu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getPrePaginatedTitle(player: Player?): String {
        return "&7Permissions Editor"
    }

    override fun getAllPagesButtons(player: Player?): MutableMap<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->
            rank.permission.forEachIndexed { index, permission ->
                toReturn[index] = object : Button() {
                    override fun getName(var1: Player?): String {
                        return "&d&lPermission #$index. "
                    }

                    override fun getMaterial(var1: Player?): Material {
                        return Material.PAPER
                    }

                    override fun getDescription(var1: Player?): MutableList<String> {
                        return arrayListOf<String>().also { toReturn ->
                            toReturn.add("&d${UnicodeUtil.VERTICAL_LINE} &fPermission&7: &d${permission}")
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "LEFT-CLICK", "to remove permission."))
                        }
                    }

                    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                        this@RankPermissions.rank.permission.remove(permission)
                        ModuleManager.rankModule.updateRank(rank)
                    }
                }
            }
        }
    }

    override fun getGlobalButtons(player: Player): MutableMap<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->
            for (i in 0..8) {
                toReturn[getSlot(i, 0)] = GlassButton(0)
                toReturn[getSlot(i, 4)] = GlassButton(0)
            }

            toReturn[4] = MenuButton()
                .icon(Material.SKULL)
                .data(3)
                .name("&d&lCreate a new permission!")
                .lore(listOf(
                    "",
                    "&7Create a new permission ",
                    "&7for this rank",
                    "",
                    "&eClick here to create a new permission!"
                ))
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    startPrompt(player, "permission")
                }

            toReturn[18] = MenuButton()
                .icon(Material.BED)
                .name("&cGo Back")
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    RankEditor(rank).openMenu(player)
                }
        }
    }

    private fun startPrompt(player: Player, type: String) {
        player.beginConversation(
            ConversationFactory(Snowfall.get())
                .withModality(true)
                .withPrefix(NullConversationPrefix())
                .withFirstPrompt(RankModifyPrompt(rank, "rank_editor", type, player))
                .withEscapeSequence("/no")
                .withLocalEcho(false)
                .withTimeout(25)
                .thatExcludesNonPlayersWithMessage("Only players can interact!")
                .buildConversation(player)
        )
    }

    override fun getMaxItemsPerPage(player: Player?): Int {
        return 8
    }

    override fun size(player: Player?): Int {
        return 18
    }
}