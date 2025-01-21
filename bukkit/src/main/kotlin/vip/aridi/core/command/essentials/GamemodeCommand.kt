package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.OptArg
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class GamemodeCommand {

    @Command(name = "gamemode", desc = "Change your gamemode or another player's gamemode.", aliases = ["gm"], usage = "[gamemode] [target]")
    @Require("snowfall.essentials.gamemode")
    fun gamemode(@Sender sender: CommandSender, gamemode: GameMode, @OptArg target: Player?) {
        val player = target ?: (sender as? Player) ?: return sender.sendMessage(CC.translate("&cYou must specify a player if you're not a player."))
        if (!validatePermission(sender, player)) return

        player.gameMode = gamemode
        sender.sendMessage(CC.translate(if (sender != player) "&6${player.displayName} is now in &f$gamemode &6mode." else "&6You are now in &f$gamemode &6mode."))
    }

    @Command(name = "gm0", desc = "Set your gamemode to Survival or another player's gamemode.", aliases = ["gms"], usage = "[target]")
    @Require("snowfall.essentials.gamemode")
    fun gm0(@Sender sender: CommandSender, @OptArg target: Player?) = gamemode(sender, GameMode.SURVIVAL, target)

    @Command(name = "gm1", desc = "Set your gamemode to Creative or another player's gamemode.", aliases = ["gmc"], usage = "[target]")
    @Require("snowfall.essentials.gamemode")
    fun gm1(@Sender sender: CommandSender, @OptArg target: Player?) = gamemode(sender, GameMode.CREATIVE, target)

    @Command(name = "gm2", desc = "Set your gamemode to Adventure or another player's gamemode.", aliases = ["gma"], usage = "[target]")
    @Require("snowfall.essentials.gamemode")
    fun gm2(@Sender sender: CommandSender, @OptArg target: Player?) = gamemode(sender, GameMode.ADVENTURE, target)

    @Command(name = "gm3", desc = "Set your gamemode to Spectator or another player's gamemode.", aliases = ["gmspect"], usage = "[target]")
    @Require("snowfall.essentials.gamemode")
    fun gm3(@Sender sender: CommandSender, @OptArg target: Player?) = gamemode(sender, GameMode.SPECTATOR, target)

    private fun validatePermission(sender: CommandSender, target: Player): Boolean {
        if (sender != target && !sender.hasPermission("core.admin")) {
            sender.sendMessage(CC.translate("&cNo permission to set other player's gamemode."))
            return false
        }
        return true
    }
}