package vip.aridi.core.rank.menus.editor

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import vip.aridi.core.Snowfall
import vip.aridi.core.rank.Rank
import vip.aridi.core.rank.menus.RankEditor
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
 * Date: 28 - nov
 */

class RankFormatter(
    private val rank: Rank
): Menu() {

    override fun getTitle(player: Player): String {
        return "&d&lRank Formatter"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        val buttons = mutableMapOf<Int, Button>()
        addGlassBorders(buttons)

        buttons[11] = rankPrefixButton(player)
        buttons[12] = rankColorButton(player)

        buttons[14] = rankDisplayButton(player)
        buttons[15] = rankNameButton()

        buttons[18] = createBackButton(player)

        return buttons
    }

    private fun rankPrefixButton(player: Player): Button {
        return MenuButton()
            .icon(Material.NAME_TAG)
            .name("&d&lRank Prefix")
            .lore(listOf(
                "",
                " &d${UnicodeUtil.VERTICAL_LINE} &fCurrent&7: ${rank.prefix}",
                "",
                "&eClick here to modify the rank prefix."
                ))
            .action(ClickType.LEFT) {
                startPrompt(player, "prefix")
            }
    }

    private fun rankColorButton(player: Player): Button {
        return MenuButton()
            .icon(Material.WOOL)
            .data(CC.getWoolData(rank.color))
            .name("&d&lRank Color")
            .lore(listOf(
                "",
                " &d${UnicodeUtil.VERTICAL_LINE} &fCurrent&7: ${ChatColor.valueOf(rank.color)}${rank.color}",
                "",
                "&eClick here to change the rank color"
            ))
            .action(ClickType.LEFT) {
                RankColor(rank).openMenu(player)
            }
    }

    private fun rankDisplayButton(player: Player): Button {
        return MenuButton()
            .icon(Material.ITEM_FRAME)
            .name("&d&lRank Display")
            .lore(listOf(
                "",
                " &d${UnicodeUtil.VERTICAL_LINE} &fCurrent&7: ${rank.prefix}"
            ))
            .action(ClickType.LEFT) {
                startPrompt(player, "displayName")
            }
    }

    private fun rankNameButton(): Button {
        return MenuButton()
            .icon(Material.SIGN)
            .name("&b&lRank Name")
            .lore(listOf(
                "",
                " &d${UnicodeUtil.VERTICAL_LINE} &fCurrent&7: ${ChatColor.valueOf(rank.color)}${rank.name}",
                "",
                "&cThis value cannot be modified."
            ))
    }

    private fun addGlassBorders(buttons: MutableMap<Int, Button>) {
        for (i in 0..26) {
            buttons[i] = GlassButton(0)
        }
    }

    private fun createBackButton(player: Player): Button {
        return MenuButton()
            .icon(Material.BED)
            .name(CC.translate("&cGo Back"))
            .action(ClickType.LEFT) {
                player.closeInventory()
                RankEditor(rank).openMenu(player)
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
}