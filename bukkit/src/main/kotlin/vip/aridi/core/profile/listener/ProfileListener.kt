package vip.aridi.core.profile.listener

import vip.aridi.core.module.ModuleManager
import vip.aridi.core.Snowfall
import org.bukkit.ChatColor
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

class ProfileListener(plugin: JavaPlugin) : vip.aridi.core.listener.oListener(plugin) {


    override fun registerEvents() {
        highPriority<AsyncPlayerPreLoginEvent> { event ->
            try {
                val profile = ModuleManager.profileModule.loadProfile(event.name)

                if (profile == null) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "${ChatColor.RED}Your profile hasn't loaded, please re-join or contact an administrator.")
                    return@highPriority
                }

                ModuleManager.profileModule.updateProfile(profile)
                Snowfall.get().logger.info("Loaded the ${profile.name}'s profile successfully!")
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        highPriority<PlayerJoinEvent> { event ->
            val player = event.player

            val profile = ModuleManager.profileModule.getProfile(player.uniqueId)

            if (profile == null) {
                player.kickPlayer("${ChatColor.RED}Your profile hasn't loaded, please re-join or contact an administrator.")
                return@highPriority
            }
        }

        highPriority<PlayerQuitEvent> { event ->
            val player = event.player

            try {
                ModuleManager.profileModule.deleteProfile(player.uniqueId)?.flagsForSave()

                Snowfall.get().logger.info("Saved the ${player.name}'s profile successfully!")
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}