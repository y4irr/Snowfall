package vip.aridi.core.rank.menus

import com.google.common.collect.Lists
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import vip.aridi.core.Snowfall
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.rank.Rank
import vip.aridi.core.rank.prompt.RankCreatePrompt
import vip.aridi.core.util.Callback
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.TextSplitter
import vip.aridi.core.utils.TextureUtil
import vip.aridi.core.utils.UnicodeUtil
import vip.aridi.core.utils.menus.ConfirmMenu
import vip.aridi.core.utils.menus.PaginatedMenu
import vip.aridi.core.utils.menus.button.Button
import vip.aridi.core.utils.menus.button.Button.*
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

class RankMenu: PaginatedMenu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getPrePaginatedTitle(player: Player?): String {
        return CC.translate("&7Rank Editor")
    }

    override fun getAllPagesButtons(player: Player?): MutableMap<Int, Button> {
        return getAllowedRanks().mapIndexed { index, rank ->
            index to createRankButton(rank)
        }.toMap().toMutableMap()
    }

    override fun getGlobalButtons(player: Player): MutableMap<Int, Button> {
        val globalButtons = mutableMapOf<Int, Button>()
        addGlassBorders(globalButtons)
        addCreateRankButton(globalButtons, player)
        return globalButtons
    }


    private fun createRankButton(rank: Rank): Button {
        return object : Button() {
            override fun getName(var1: Player?): String {
                return CC.translate(rank.displayName)
            }

            override fun getDescription(var1: Player): MutableList<String> {
                return buildRankDescription(rank)
            }

            override fun getMaterial(var1: Player?): Material {
                return Material.WOOL
            }

            override fun getDamageValue(player: Player?): Byte {
                return CC.getWoolData(rank.color)
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                handleRankClick(player, rank, clickType)
            }

        }
    }

    private fun buildRankDescription(rank: Rank): MutableList<String> {
        val description = mutableListOf<String>()

        description.add("")
        description.add("&d${UnicodeUtil.VERTICAL_LINE} &fColor&7: ${ChatColor.valueOf(rank.color)}${CC.convert(rank.color)}")
        if (rank.name != ModuleManager.rankModule.defaultRank.name) {
            description.add("&d${UnicodeUtil.VERTICAL_LINE} &fPrefix&7: &d${rank.prefix}")
        }
        description.add(" &d${UnicodeUtil.VERTICAL_LINE} &fWeight&7: &d${rank.priority}")
        description.add(" &d${UnicodeUtil.VERTICAL_LINE} &fPermissions&7: &d${rank.permission.size}")
        description.add("")
        description.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to manage rank"))
        description.add(styleAction(ChatColor.RED, "RIGHT-CLICK", "to delete rank"))
        return CC.translateMutable(description)
    }

    private fun handleRankClick(player: Player, rank: Rank, clickType: ClickType) {
        if (clickType.isRightClick) {
            if (rank.name == ModuleManager.rankModule.defaultRank.name) {
                player.sendMessage(CC.translate("&cRank '${rank.name}' is not deletable."))
                playFail(player)
                return
            }
            confirmRankDeletion(player, rank)
        } else if (clickType.isLeftClick) {
            player.closeInventory()
            RankEditor(rank).openMenu(player)
        }
    }

    private fun addGlassBorders(buttons: MutableMap<Int, Button>) {
        for (i in 0..8) {
            buttons[getSlot(i, 0)] = GlassButton(0)
            buttons[getSlot(i, 4)] = GlassButton(0)
        }
    }

    private fun addCreateRankButton(buttons: MutableMap<Int, Button>, player: Player) {
        val createButton = MenuButton().apply {
            name(CC.translate("&aCreate new rank"))
            lore(buildCreateRankLore())
            action(ClickType.LEFT) {
                player.closeInventory()
                startRankCreationPrompt(player)
            }
        }

        createButton.texturedIcon(TextureUtil.GREEN_PLUS_TEXTURE)

        buttons[4] = createButton
    }

    private fun buildCreateRankLore(): List<String> {
        return mutableListOf<String>().apply {
            add("")
            addAll(TextSplitter.split(text = "Create a new rank following the creation instructions"))
            add("")
            add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to create a new Rank!"))
        }
    }

    private fun startRankCreationPrompt(player: Player) {
        ConversationFactory(Snowfall.get())
            .withModality(true)
            .withPrefix(NullConversationPrefix())
            .withFirstPrompt(RankCreatePrompt(player))
            .withEscapeSequence("/no")
            .withLocalEcho(false)
            .withTimeout(25)
            .thatExcludesNonPlayersWithMessage(CC.translate("&cOnly players can interact with this process!"))
           .buildConversation(player)
           .begin()

    }

    private fun confirmRankDeletion(player: Player, rank: Rank) {
        playNeutral(player)
        ConfirmMenu(
            "Are you sure?",
            object : Callback<Boolean> {
                override fun callback(callback: Boolean) {
                    if (callback) {
                        player.sendMessage(CC.translate("&aRank '${rank.displayName}&a' is now successfully deleted."))
                        ModuleManager.rankModule.deleteRank(rank.name)
                        ModuleManager.rankModule.cache.remove(rank.name)
                    }

                    player.closeInventory()
                    RankMenu().openMenu(player)
                }
            }
        ).openMenu(player)
    }

    private fun getAllowedRanks(): List<Rank> {
        val allRanks = ModuleManager.rankModule.findAllRanks()
        val ranks: MutableList<Rank> = Lists.newArrayList()
        ranks.addAll(allRanks)
        ranks.sortWith { o1, o2 -> o2.priority - o1.priority }
        return ranks
    }
}