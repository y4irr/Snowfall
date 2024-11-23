package vip.aridi.core.provider

import com.jonahseguin.drink.argument.CommandArg
import com.jonahseguin.drink.exception.CommandExitMessage
import com.jonahseguin.drink.parametric.DrinkProvider
import vip.aridi.core.utils.Duration

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 23 - nov
 */

class DurationProvider : DrinkProvider<Duration>() {

    override fun doesConsumeArgument(): Boolean = true

    override fun isAsync(): Boolean = false

    override fun allowNullArgument(): Boolean = false

    override fun provide(arg: CommandArg, annotations: MutableList<out Annotation>): Duration {
        val input = arg.get()

        return try {
            Duration.parse(input)
        } catch (e: IllegalArgumentException) {
            throw CommandExitMessage("Invalid duration, use '1h', '30m', or 'perm' for permanent.")
        }
    }

    override fun argumentDescription(): String = "Duration (e.x., 1h, 30m, perm)"

    override fun getSuggestions(prefix: String): List<String> {
        return listOf("1h", "30m", "perm")
    }
}