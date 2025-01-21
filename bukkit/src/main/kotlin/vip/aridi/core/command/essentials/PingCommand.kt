package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.*
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
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

class PingCommand {

    @Command(
        name = "ping",
        desc = "Check your ping or another player's ping.",
        aliases = ["latency", "ms"],
        usage = "[player]"
    )
    fun ping(
        @Sender sender: CommandSender,
        @OptArg target: Player?
    ) {
        if (target == null) {
            if (sender is Player) {
                sender.sendMessage(CC.translate("&fYour Ping: &b${getPing(sender)} ms"))
            } else {
                sender.sendMessage(CC.translate("&cOnly players can check their own ping."))
            }
            return
        }

        sender.sendMessage(CC.translate("&6${target.displayName} &fhas a ping of: &e${getPing(target)} &fms."))
    }

    private fun getPing(player: Player): Int {
        return (player as CraftPlayer).handle.ping
    }
}