package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Require
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.World
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class KillAllCommand {

    @Command(
        name = "killall",
        desc = "Remove all non-player entities from all worlds.",
        aliases = ["clearmobs", "clearentities", "killentities"],
        usage = ""
    )
    @Require("core.admin")
    fun killAllCommand(@Sender sender: CommandSender) {
        Bukkit.getWorlds().forEach { world ->
            val removedEntities = clearEntities(world)
            sender.sendMessage(CC.translate("&6Cleared &f$removedEntities &6entities from &f${world.name}&6."))
        }
    }

    private fun clearEntities(world: World): Int {
        return world.entities.filter { it.type != EntityType.PLAYER }.onEach { it.remove() }.size
    }
}
