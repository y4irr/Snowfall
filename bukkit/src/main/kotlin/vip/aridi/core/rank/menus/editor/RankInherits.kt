package vip.aridi.core.rank.menus.editor

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import vip.aridi.core.module.ModuleManager
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
 * Date: 23 - nov
 */

class RankInherits(
    private val rank: Rank
): PaginatedMenu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getPrePaginatedTitle(player: Player?): String {
        return "&7Inherits Editor"
    }

    override fun getAllPagesButtons(player: Player?): MutableMap<Int, Button> {
        val toReturn = mutableMapOf<Int, Button>()

        ModuleManager.rankModule.findAllRanks()
            .filter { it.name != rank.name }
            .sortedBy { it.priority }
            .forEach { rankOption ->
                toReturn[toReturn.size] = object : Button() {
                    override fun getName(player: Player): String {
                        return CC.translate(rankOption.displayName)
                    }

                    override fun getMaterial(player: Player): Material {
                        return Material.WOOL
                    }

                    override fun getDamageValue(player: Player): Byte {
                        if (rankOption.color.isEmpty()) {
                            return 0
                        }
                        return CC.getWoolData(rankOption.color)
                    }

                    override fun getDescription(player: Player): MutableList<String> {
                        return arrayListOf<String>().also { description ->
                            if (rank.inheritance.contains(rankOption.name)) {
                                description.add("")
                                description.add(styleAction(ChatColor.RED, "LEFT-CLICK", "to remove inherit."))
                            } else {
                                description.add("")
                                description.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to add inherit."))
                            }
                        }
                    }

                    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                        if (!rank.inheritance.contains(rankOption.name)) {
                            rank.inheritance.add(rankOption.name)
                            playNeutral(player)
                        } else {
                            rank.inheritance.remove(rankOption.name)
                            playNeutral(player)
                        }

                        ModuleManager.rankModule.updateRank(rank)
                    }

                }
            }

        return toReturn
    }

    override fun getGlobalButtons(player: Player?): MutableMap<Int, Button> {
        return hashMapOf<Int, Button>().also { buttons ->
            for (i in 0..8) {
                buttons[getSlot(i, 0)] = GlassButton(0)
                buttons[getSlot(i, 4)] = GlassButton(0)
            }
        }
    }
}