package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.module.SharedManager
import vip.aridi.core.rank.Rank
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class ListCommand {

    @Command(name = "", desc = "List online players and their ranks.")
    fun listCommand(@Sender sender: CommandSender) {
        val visibleRanks = SharedManager.rankModule.cache.values
            .filter { canSee(sender, it) }
            .sortedByDescending { it.priority }
            .joinToString(separator = "${CC.WHITE}, ") { it.displayName }

        sender.sendMessage(CC.translate(visibleRanks))

        val players = Bukkit.getServer().onlinePlayers
            .filter { sender !is Player || sender.canSee(it) }
            .map { player -> player to SharedManager.grantModule.findGrantedRank(player.uniqueId) }
            .sortedByDescending { it.second.priority }

        val playersFormatted = players.joinToString(separator = "&f, ") {
            formatName(it.first, it.second)
        }

        sender.sendMessage(CC.translate("&f(${players.size}/${Bukkit.getServer().maxPlayers}) [&f$playersFormatted&f]"))
    }

    private fun canSee(sender: CommandSender, rank: Rank): Boolean {
        return sender.isOp || !rank.hidden || sender.hasPermission("snowfall.grant.rank.${rank.name}")
    }

    private fun formatName(player: Player, rank: Rank): String {
        return "${rank.color}${player.name}"
    }
}
