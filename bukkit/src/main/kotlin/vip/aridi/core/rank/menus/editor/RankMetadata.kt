package vip.aridi.core.rank.menus.editor

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import vip.aridi.core.Snowfall
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.rank.Rank
import vip.aridi.core.rank.menus.RankEditor
import vip.aridi.core.rank.prompt.RankModifyPrompt
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.UnicodeUtil
import vip.aridi.core.utils.menus.Menu
import vip.aridi.core.utils.menus.button.Button
import vip.aridi.core.utils.menus.button.Button.styleAction
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

class RankMetadata(
    private val rank: Rank
): Menu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getTitle(player: Player): String {
        return "&7Metadata Editor"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        val buttons = mutableMapOf<Int, Button>()
        addGlassBorders(buttons)

        buttons[11] = createPriorityButton()

        buttons[13] = createPriceButton(player)

        buttons[15] = createMetadataButton(
            "Discord ID",
            Material.NAME_TAG,
            rank.discordId ?: "${ChatColor.RED}Not Set",
            "discordId",
            player
        )

        buttons[21] = createToggleButton("Grantable", Material.LEVER, rank.canBeGrantable) {
            rank.canBeGrantable = !rank.canBeGrantable
            saveRank()
        }

        buttons[22] = createToggleButton("Staff", Material.BEACON, rank.staff) {
            rank.staff = !rank.staff
            saveRank()
        }

        buttons[23] = createToggleButton("Donator", Material.DIAMOND, rank.donator) {
            rank.donator = !rank.donator
            saveRank()
        }

        buttons[29] = createToggleButton("Media", Material.QUARTZ, rank.media) {
            rank.media = !rank.media
            saveRank()
        }

        buttons[31] = createToggleButton("Hidden", Material.SUGAR, rank.hidden) {
            rank.hidden = !rank.hidden
            saveRank()
        }

        buttons[33] = createToggleButton("Default", Material.EYE_OF_ENDER, rank.defaultRank) {
            rank.defaultRank = !rank.defaultRank
            saveRank()
        }

        buttons[36] = MenuButton()
            .icon(Material.BED)
            .name("&cGo Back")
            .action(ClickType.LEFT) {
                player.closeInventory()
                RankEditor(rank).openMenu(player)
            }

        return buttons
    }

    override fun size(player: Player?): Int {
        return 54
    }

    private fun addGlassBorders(buttons: MutableMap<Int, Button>) {
        for (i in 0..8) {
            buttons[getSlot(i, 0)] = GlassButton(0)
            buttons[getSlot(i, 4)] = GlassButton(0)
        }
        for (i in 0..3) {
            buttons[getSlot(0, i)] = GlassButton(0)
            buttons[getSlot(8, i)] = GlassButton(0)
        }
    }

    private fun createPriorityButton(): Button {
        return MenuButton()
            .icon(Material.GOLD_NUGGET)
            .name(CC.translate("&d&lPriority"))
            .lore(buildPriorityLore())
            .action(ClickType.LEFT) {
                rank.priority++
                saveRank()
            }
            .action(ClickType.RIGHT) {
                if (rank.priority > 0) {
                    rank.priority--
                    saveRank()
                } else {
                    notifyPlayerLimitExceeded()
                }
            }
            .action(ClickType.MIDDLE) {
                rank.priority = 1
                saveRank()
            }
            .action(ClickType.SHIFT_LEFT) {
                rank.priority += 10
                saveRank()
            }
            .action(ClickType.SHIFT_RIGHT) {
                if (rank.priority >= 10) {
                    rank.priority -= 10
                    saveRank()
                } else {
                    notifyPlayerLimitExceeded()
                }
            }
    }

    private fun notifyPlayerLimitExceeded() {
        rank.priority = 0
    }

    private fun createPriceButton(player: Player): Button {
        return MenuButton()
            .icon(Material.PAPER)
            .name(CC.translate("&d&lPrice"))
            .lore(buildPriceLore())
            .action(ClickType.LEFT) {
                player.closeInventory()
                startPrompt(player, "price")
            }
    }

    private fun buildPriorityLore(): List<String> = CC.translateList(listOf(
        "",
        " &7${UnicodeUtil.VERTICAL_LINE} &dCurrent&7: &b${rank.priority}",
        "",
        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to add 1 priority."),
        styleAction(ChatColor.GREEN, "SHIFT-LEFT", "to add 10 priority."),
        styleAction(ChatColor.RED, "RIGHT-CLICK", "to subtract 1 priority."),
        styleAction(ChatColor.RED, "SHIFT-RIGHT", "to subtract 10 priority."),
        styleAction(ChatColor.YELLOW, "MIDDLE-CLICK", "to reset priority.")
    ))

    private fun buildPriceLore(): List<String> = CC.translateList(listOf(
        "",
        " &7${UnicodeUtil.VERTICAL_LINE} &dCurrent&7: &a${UnicodeUtil.COINS}${rank.price}",
        "",
        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify price.")
    ))

    private fun createMetadataButton(
        name: String,
        icon: Any,
        currentValue: String,
        metadataType: String,
        player: Player
    ): Button {
        val materialIcon = when (icon) {
            is Material -> ItemStack(icon)
            is ItemStack -> icon
            else -> throw IllegalArgumentException("Invalid icon type")
        }
        return MenuButton()
            .icon(materialIcon)
            .name("&e&l$name")
            .lore(
                listOf(
                    "",
                    " &d${UnicodeUtil.VERTICAL_LINE} &fCurrent&7: &d$currentValue",
                    "",
                    styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify $name.")
                )
            )
            .action(ClickType.LEFT) {
                startPrompt(player, metadataType)
            }
    }

    private fun createToggleButton(
        name: String,
        icon: Material,
        currentValue: Boolean,
        toggleAction: () -> Unit
    ): Button {
        return MenuButton()
            .icon(icon)
            .name("&e&l$name")
            .lore(
                listOf(
                    "",
                    " &7${UnicodeUtil.VERTICAL_LINE} &eCurrent&7: ${if (currentValue) "&aEnabled" else "&cDisabled"}",
                    "",
                    styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify $name status.")
                )
            )
            .action(ClickType.LEFT) {
                toggleAction()
            }
    }

    private fun startPrompt(player: Player, type: String) {
        player.closeInventory()
        player.beginConversation(
            ConversationFactory(Snowfall.get())
                .withModality(true)
                .withPrefix(NullConversationPrefix())
                .withFirstPrompt(RankModifyPrompt(rank, "metadata", type, player))
                .withEscapeSequence("/no")
                .withLocalEcho(false)
                .withTimeout(25)
                .thatExcludesNonPlayersWithMessage("&cOnly players can interact!")
                .buildConversation(player)
        )
    }

    private fun saveRank() {
        ModuleManager.rankModule.updateRank(rank)
    }
}