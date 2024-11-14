package vip.aridi.core.command

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

class TwitterCommand {

    @Command(name = "", aliases = ["twitter"], desc = "")
    fun onCommand(@Sender sender: CommandSender) {
        for (s in ModuleManager.configModule.messagesConfig.config.getStringList("MESSAGES.X")) {
            sender.sendMessage(CC.translate(s.replace("{twitter}", ModuleManager.configModule.mainConfig.config.getString("LINKS.X"))))
        }
    }
}