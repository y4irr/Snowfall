package vip.aridi.core.provider

import com.jonahseguin.drink.argument.CommandArg
import com.jonahseguin.drink.exception.CommandExitMessage
import com.jonahseguin.drink.parametric.DrinkProvider
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2025
 * Date: 21 - jan
 */

class PlayerProvider : DrinkProvider<Player>() {

    override fun doesConsumeArgument(): Boolean = true

    override fun isAsync(): Boolean = false

    override fun allowNullArgument(): Boolean = false

    override fun provide(arg: CommandArg, annotations: MutableList<out Annotation>): Player {
        val playerName = arg.get()

        val player = Bukkit.getPlayer(playerName)
            ?: throw CommandExitMessage("No player found with that name: $playerName.")

        return player
    }

    override fun argumentDescription(): String = "Player Name"

    override fun getSuggestions(prefix: String): List<String> {
        return Bukkit.getOnlinePlayers()
            .map { it.name }
            .filter { it.startsWith(prefix, ignoreCase = true) }
    }
}