package vip.aridi.core.command.admin.grants

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.grant.menu.apply.GrantMenu
import vip.aridi.core.profile.Profile
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

class GrantCommand {

    @Command(
        name = "",
        desc = "View all granted ranks of player"
    )
    @Require(
        "snowfall.admin.grant"
    )
    fun grants(
        @Sender sender: CommandSender,
        profile: Profile
    ) {
        if (sender !is Player) {
            sender.sendMessage(CC.translate("&cYou must be a player to perform that command"))
            return
        }

        GrantMenu(profile).openMenu(sender)
        sender.playSound(sender.location, Sound.NOTE_PLING, 0.2f, 1.5f)
    }
}