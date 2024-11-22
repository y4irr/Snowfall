package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.command.CommandSender
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class DiscordCommand {

    @Command(name = "", aliases = ["dc"], desc = "")
    fun onCommand(@Sender sender: CommandSender) {
        for (s in ModuleManager.configModule.messagesConfig.config.getStringList("MESSAGES.DISCORD")) {
            sender.sendMessage(CC.translate(s.replace("{discord}", ModuleManager.configModule.mainConfig.config.getString("LINKS.DISCORD"))))
        }
    }
}