package vip.aridi.core.command.essentials

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

import com.jonahseguin.drink.annotation.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import vip.aridi.core.utils.CC

class TeleportationCommand {

    @Command(
        name = "teleport",
        desc = "Teleport yourself to a player.",
        aliases = ["tp", "tpto", "goto"],
        usage = "<player>"
    )
    @Require("core.staff")
    fun teleport(
        @Sender sender: CommandSender,
        target: Player
    ) {
        if (sender !is Player) {
            sender.sendMessage(CC.translate("&cOnly players can use this command."))
            return
        }

        if (sender.world != target.world) {
            sender.teleport(target.world.spawnLocation)
        }
        sender.teleport(target)
        sender.sendMessage(CC.translate("&6Teleporting you to &f${target.displayName}&6."))
    }

    @Command(
        name = "tphere",
        desc = "Teleport a player to you.",
        aliases = ["bring", "s"],
        usage = "<player>"
    )
    @Require("core.staff")
    fun tphere(@Sender sender: CommandSender,
               @Flag('s') silent: Boolean,
               target: Player
    ) {
        if (sender !is Player) {
            sender.sendMessage(CC.translate("&cOnly players can use this command."))
            return
        }

        if (sender.world != target.world) {
            target.teleport(sender.world.spawnLocation)
        }
        target.teleport(sender.location)

        sender.sendMessage(CC.translate("&6Teleporting &f${target.displayName} &6to you."))
        if (!silent || target.hasPermission("core.staff")) {
            target.sendMessage(CC.translate("&6Teleporting you to &f${sender.displayName}&6."))
        }
    }

    @Command(
        name = "tpall",
        desc = "Teleport all players to your location.",
        aliases = ["teleportationall", "teleportall"],
        usage = ""
    )
    @Require("core.admin")
    fun teleportAll(
        @Sender sender: CommandSender
    ) {
        if (sender !is Player) {
            sender.sendMessage(CC.translate("&cOnly players can use this command."))
            return
        }

        Bukkit.getOnlinePlayers().forEach { player ->
            if (player != sender) {
                player.teleport(sender.location)
                player.sendMessage(CC.translate("&6You were teleported to &f${sender.displayName}&6's location."))
            }
        }
        sender.sendMessage(CC.translate("&6All players were teleported to your location."))
    }

    @Command(
        name = "tppos",
        desc = "Teleport to coordinates.",
        aliases = ["teleportposition", "teleportpos", "tpos"],
        usage = "<x> <y> <z> [player]"
    )
    @Require("core.staff")
    fun teleportPosition(
        @Sender sender: CommandSender,
        x: Double, y: Double, z: Double,
        @OptArg target: Player?
    ) {
        val player = target ?: (sender as? Player) ?: run {
            sender.sendMessage(CC.translate("&cYou must specify a player or be a player yourself."))
            return
        }

        val adjustedX = if (isBlock(x)) x + if (z >= 0) 0.5 else -0.5 else x
        val adjustedZ = if (isBlock(z)) z + if (x >= 0) 0.5 else -0.5 else z

        player.teleport(Location(player.world, adjustedX, y, adjustedZ))
        val locationString = CC.translate("&e[&f$adjustedX, $y, $adjustedZ&e]")

        sender.sendMessage(CC.translate("&6Teleporting &f${player.displayName} &6to $locationString."))
        if (sender != player) {
            player.sendMessage(CC.translate("&6You have been teleported to $locationString."))
        }
    }

    private fun isBlock(value: Double): Boolean {
        return value % 1.0 == 0.0
    }
}
