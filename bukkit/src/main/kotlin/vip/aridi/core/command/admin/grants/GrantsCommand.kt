package vip.aridi.core.command.admin.grants

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.grant.menu.view.GrantsMenu
import vip.aridi.core.module.ModuleManager
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

class GrantsCommand {

    @Command(name = "grants", desc = "View all granted ranks of player")
    @Require("snowfall.admin.grants")
    fun grants(
        @Sender sender: CommandSender,
        profile: Profile
    ) {
        if (sender !is Player) return
        val grants = ModuleManager.grantModule.findAllByPlayer(profile.id)

        if (grants.isEmpty()) {
            sender.sendMessage(CC.translate("${profile.name}&c has no grants."))
            return
        }

        GrantsMenu(grants, profile).openMenu(sender)
        sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
    }
}