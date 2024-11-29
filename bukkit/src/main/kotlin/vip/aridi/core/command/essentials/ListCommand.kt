package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Sender
import org.apache.commons.lang3.StringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.module.ModuleManager
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

    @Command(name = "",
        desc = ""
    )
    fun listCommand(@Sender sender: CommandSender) {
        sender.sendMessage(CC.translate(StringUtils.join(ModuleManager.rankModule.cache.values
            .filter { canSee(sender, it) }
            .sortedBy { it.priority }
            .reversed()
            .map { it.displayName }, "${ChatColor.WHITE}, ")))

        val players = Bukkit.getServer().onlinePlayers
            .filter { if (sender is Player) return@filter sender.canSee(it) else return@filter true }
            .sortedBy {
                val priority = ModuleManager.grantModule.findGrantedRank(it.uniqueId).priority

                return@sortedBy priority
            }
            .reversed()
            .map { formatName(it, ModuleManager.grantModule.findGrantedRank(it.uniqueId)) }

        sender.sendMessage(
            CC.translate("&f(${players.size}/${Bukkit.getServer().maxPlayers}) [${StringUtils.join(players, "&f,")}&f]")
        )
    }

    private fun canSee(sender: CommandSender, rank: Rank): Boolean {
        if (sender.isOp || !rank.hidden) return true

        return sender.hasPermission("snowfall.grant.rank.${rank.name}")
    }

    private fun formatName(player: Player, rank: Rank): String {
        return "${ChatColor.valueOf(rank.color)}${player.name}"
    }
}