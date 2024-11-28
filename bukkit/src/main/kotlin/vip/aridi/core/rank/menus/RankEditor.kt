package vip.aridi.core.rank.menus

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import vip.aridi.core.Snowfall
import vip.aridi.core.rank.Rank
import vip.aridi.core.rank.menus.editor.*
import vip.aridi.core.rank.prompt.RankModifyPrompt
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.UnicodeUtil
import vip.aridi.core.utils.menus.Menu
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

class RankEditor(
    private val rank: Rank
) : Menu() {

    init {
        isUpdateAfterClick = true
        isAutoUpdate = true
    }

    override fun getTitle(player: Player): String {
        return CC.translate("${ChatColor.valueOf(rank.color)}${rank.name}&7's Settings")
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        val buttons = mutableMapOf<Int, Button>()
        addGlassBorders(buttons)

        buttons[11] = rankFormatterButton(player)

        buttons[13] = rankPermissionsButton(player)

        buttons[15] = rankMetadataButton(player)

        buttons[18] = createBackButton(player)

        return buttons
    }

    override fun size(player: Player): Int {
        return 9 * 3
    }

    private fun rankFormatterButton(player: Player): Button {
        return MenuButton()
            .icon(Material.SIGN)
            .name("&d&lRank Formatter")
            .lore(listOf(
                "",
                "&eClick here to manage the rank format values",
                ""
            ))
            .action(ClickType.LEFT) {
                player.closeInventory()
                RankFormatter(rank).openMenu(player)
            }
    }

    private fun rankPermissionsButton(player: Player): Button {
        return MenuButton()
            .icon(Material.IRON_FENCE)
            .name("&d&lRank Permissions")
            .lore(listOf(
                "",
                " &d${UnicodeUtil.VERTICAL_LINE} &fCurrent&7: &d${rank.permission.size}",
                "",
                "&eClick here to manage the rank permissions"
            ))
            .action(ClickType.LEFT) {
                player.closeInventory()
                startPrompt(player, "permission")
            }
            .action(ClickType.RIGHT) {
                player.closeInventory()
                RankPermissions(rank).openMenu(player)
            }
    }

    private fun rankMetadataButton(player: Player): Button {
        return MenuButton()
            .icon(Material.HOPPER)
            .name("&d&lRank Metadata")
            .lore(listOf(
                "",
                "&eClick here to modify the metadata of the rank",
                ""
            ))
            .action(ClickType.LEFT) {
                player.closeInventory()
                RankMetadata(rank).openMenu(player)
            }
    }

    private fun createBackButton(player: Player): Button {
        return MenuButton()
            .icon(Material.BED)
            .name(CC.translate("&cGo Back"))
            .action(ClickType.LEFT) {
                player.closeInventory()
                RankMenu().openMenu(player)
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

    private fun addGlassBorders(buttons: MutableMap<Int, Button>) {
        for (i in 0..8) {
            buttons[getSlot(i, 0)] = GlassButton(0)
            buttons[getSlot(i, 2)] = GlassButton(0)
        }
        for (i in 0..2) {
            buttons[getSlot(0, i)] = GlassButton(0)
            buttons[getSlot(8, i)] = GlassButton(0)
        }
    }

}