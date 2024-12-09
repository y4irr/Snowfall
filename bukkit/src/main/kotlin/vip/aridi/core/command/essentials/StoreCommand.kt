package vip.aridi.core.command.essentials

import com.jonahseguin.drink.annotation.Command
import com.jonahseguin.drink.annotation.Sender
import org.bukkit.command.CommandSender
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.utils.CC

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class StoreCommand {

    @Command(name = "", desc = "")
    fun onCommand(@Sender sender: CommandSender) {
        for (s in BukkitManager.configModule.messagesConfig.config.getStringList("MESSAGES.STORE")) {
            sender.sendMessage(CC.translate(s.replace("{store}", BukkitManager.configModule.mainConfig.config.getString("LINKS.STORE"))))
        }
    }
}