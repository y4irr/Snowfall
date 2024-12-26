package vip.aridi.core.profile.listener

import vip.aridi.core.module.BukkitManager
import vip.aridi.core.Snowfall
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
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

class ProfileListener() : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onAsyncPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
        try {
            val profile = BukkitManager.profileModule.loadProfile(event.name)

            if (profile == null) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "${ChatColor.RED}Your profile hasn't loaded, please re-join or contact an administrator.")
                return
            }

            BukkitManager.profileModule.updateProfile(profile)
            Snowfall.get().logger.info("Loaded the ${profile.name}'s profile successfully!")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        val profile = BukkitManager.profileModule.getProfile(player.uniqueId)

        if (profile == null) {
            player.kickPlayer("${ChatColor.RED}Your profile hasn't loaded, please re-join or contact an administrator.")
            return
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        try {
            BukkitManager.profileModule.deleteProfile(player.uniqueId).let { BukkitManager.profileModule.toSave(it!!) }

            Snowfall.get().logger.info("Saved the ${player.name}'s profile successfully!")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}