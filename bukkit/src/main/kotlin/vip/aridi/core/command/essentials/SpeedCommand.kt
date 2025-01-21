package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.OptArg
import com.jonahseguin.drink.annotation.Sender
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

class SpeedCommand {

    @Command(
        name = "speed",
        desc = "Change your walk or fly speed.",
        aliases = ["spd"],
        usage = "[speed]"
    )
    fun speed(
        @Sender sender: CommandSender,
        @OptArg("1") speed: Int) {
        if (sender !is Player) {
            sender.sendMessage(CC.translate("&cOnly players can use this command."))
            return
        }

        if (speed < 0 || speed > 10) {
            sender.sendMessage(CC.translate("&cSpeed must be between 0 and 10."))
            return
        }

        val isFlying = sender.isFlying
        if (isFlying) {
            sender.flySpeed = calculateSpeed(speed, true)
        } else {
            sender.walkSpeed = calculateSpeed(speed, false)
        }

        sender.sendMessage(
            CC.translate("&6${if (isFlying) "Fly" else "Walk"} speed set to &f$speed&6.")
        )
    }

    private fun calculateSpeed(speed: Int, isFlying: Boolean): Float {
        val defaultSpeed = if (isFlying) 0.1f else 0.2f
        val maxSpeed = 1.0f
        return when (speed) {
            0 -> 0.0f
            10 -> maxSpeed
            else -> defaultSpeed + ((speed - 1) / 9.0f * (maxSpeed - defaultSpeed)).toFloat()
        }
    }
}
