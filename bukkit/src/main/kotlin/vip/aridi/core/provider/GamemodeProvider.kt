package vip.aridi.core.provider

import com.jonahseguin.drink.argument.CommandArg
import com.jonahseguin.drink.exception.CommandExitMessage
import com.jonahseguin.drink.parametric.DrinkProvider
import org.bukkit.entity.Player
import org.bukkit.GameMode
import org.bukkit.ChatColor

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2025
 * Date: 21 - jan
 */

class GamemodeProvider : DrinkProvider<GameMode>() {

    override fun doesConsumeArgument(): Boolean = true

    override fun isAsync(): Boolean = false

    override fun allowNullArgument(): Boolean = false

    override fun provide(arg: CommandArg, annotations: MutableList<out Annotation>): GameMode {
        val source = arg.get()

        if (source == "-0*toggle*0-" && arg.sender is Player) {
            val player = arg.sender as Player
            return if (player.gameMode != GameMode.CREATIVE) {
                GameMode.CREATIVE
            } else {
                GameMode.SURVIVAL
            }
        }

        val mode = GameMode.entries.find {
            it.name.equals(source, ignoreCase = true) ||
                    it.value.toString().equals(source, ignoreCase = true)
        }

        return mode ?: throw CommandExitMessage("${ChatColor.RED}No gamemode with the name $source found.")
    }

    override fun argumentDescription(): String = "GameMode"

    override fun getSuggestions(prefix: String): List<String> {
        return GameMode.entries
            .filter { it.name.startsWith(prefix, ignoreCase = true) }
            .map { it.name }
    }
}