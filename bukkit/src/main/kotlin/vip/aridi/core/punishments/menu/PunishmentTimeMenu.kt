package vip.aridi.core.punishments.menu

import org.bukkit.entity.Player
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.menus.Menu
import vip.aridi.core.utils.menus.button.Button
import vip.aridi.core.utils.menus.button.impl.MenuButton

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 26 - dic
 */

class PunishmentTimeMenu(toBePunished: Player, punisher: Player): Menu() {

    override fun getButtons(player: Player?): MutableMap<Int, Button> {
        val buttons = mutableMapOf<Int, Button>()

        buttons[11] = MenuButton()
            .name("&a7 Days")
            .

        buttons[13] = MenuButton()
            .name("&e15 Days")

        buttons[15] = MenuButton()
            .name("&630 Days")

        buttons[21] = MenuButton()
            .name("&cPermanent Duration")

        buttons[25] = MenuButton()
            .name("&dCustom Duration")


        return buttons
    }

    override fun getTitle(player: Player?): String {
        return CC.translate("&dSelect the duration of the punishment")
    }
}