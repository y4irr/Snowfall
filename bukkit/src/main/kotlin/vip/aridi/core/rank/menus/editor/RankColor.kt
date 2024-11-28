package vip.aridi.core.rank.menus.editor

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import vip.aridi.core.rank.Rank
import vip.aridi.core.rank.menus.editor.impl.ColorButton
import vip.aridi.core.utils.menus.PaginatedMenu
import vip.aridi.core.utils.menus.button.Button

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 23 - nov
 */

class RankColor(
    private val rank: Rank
): PaginatedMenu() {
    override fun getPrePaginatedTitle(player: Player?): String {
        return "&7Color Editor"
    }

    override fun getAllPagesButtons(player: Player?): MutableMap<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->
            val slot = 0
            for (color in ChatColor.entries) {
                if (!color.isColor) {
                    continue
                }

                val basicColor = ChatColor.getByChar(color.toString()[1])
                toReturn[slot] = ColorButton(rank, basicColor)
            }

        }
    }
}