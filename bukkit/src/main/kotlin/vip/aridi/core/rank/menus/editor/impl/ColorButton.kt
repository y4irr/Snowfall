package vip.aridi.core.rank.menus.editor.impl

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.rank.Rank
import vip.aridi.core.rank.menus.RankEditor
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.UnicodeUtil
import vip.aridi.core.utils.menus.button.Button

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 23 - nov
 */

class ColorButton(
    private val rank: Rank,
    private val color: ChatColor
): Button() {
    override fun getName(var1: Player?): String {
        return "${color}${CC.convert(color.name)} Color"
    }

    override fun getDescription(var1: Player?): List<String> {
        return arrayListOf<String>().also { description ->
            description.add("")
            description.add(" &d${UnicodeUtil.VERTICAL_LINE} &fPreview Color&7: $color${rank.name}")
            description.add("")
            description.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to select color."))
        }
    }

    override fun getMaterial(var1: Player?): Material {
        return Material.WOOL
    }

    override fun getDamageValue(player: Player?): Byte {
        return CC.getWoolData(color.name)
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        player.closeInventory()
        rank.color = color.name
        SharedManager.rankModule.updateRank(rank)

        RankEditor(rank).openMenu(player)
    }
}