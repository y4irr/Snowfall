package vip.aridi.core.listener

import com.google.gson.JsonObject
import com.mongodb.client.model.Filters
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import vip.aridi.core.Shared
import vip.aridi.core.Snowfall
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.ModuleLifecycleManager
import vip.aridi.core.module.SharedManager
import vip.aridi.core.module.impl.core.ProfileModule
import vip.aridi.core.profile.Profile
import vip.aridi.core.utils.CC
import vip.aridi.star.event.StarEvent
import java.util.*
import kotlin.math.acos

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author YairSoto © 2024
 * Date: 11 - dic
 */

class BukkitListener(plugin: Snowfall) : oListener(plugin) {
    override fun registerEvents() {
        highPriority<AsyncPlayerPreLoginEvent> { e ->
            if (e.name == null) return@highPriority

            if (e.name.length > 16 || e.name.length < 3) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "${ChatColor.RED} Your name is too short or long for join, change your nickname")
                return@highPriority
            }
            val player = Snowfall.get().server.getPlayer(e.uniqueId)

            if (player != null) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You logged in too fast or you're a bot, please re-join.")
                return@highPriority
            }

            val doc = SharedManager.databaseModule.getCollection("profiles").find(Filters.eq("_id", e.uniqueId.toString())).first()
            val profile: Profile = if (doc == null) Profile(e.uniqueId, e.name) else {
                try {
                    SharedManager.databaseModule.gson.fromJson(doc.toJson(), Profile::class.java)
                } catch (ex: Exception) {
                    e.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
                    e.kickMessage = "${ChatColor.RED}We're having issues loading your profile, please try again later."
                 throw ex
                }
            }

            val ip = e.address.hostAddress
            profile.online = true

            val updateName = profile.name != e.name || !BukkitManager.profileModule.isProfiled(e.uniqueId)
            val updateAddress = !profile.addresses.contains(ip)

            if (updateName || updateAddress) {
                if (updateAddress) {
                    profile.address = ip
                    profile.addresses.add(ip)
                }

                Bukkit.getServer().scheduler.runTaskAsynchronously(Snowfall.get()) {
                    if (updateName) {
                        val payload = JsonObject().apply {
                            addProperty("_id", e.uniqueId.toString())
                            addProperty("name", e.name)
                        }

                        BukkitManager.profileModule.updateId(e.uniqueId, e.name)
                        SharedManager.databaseModule.redisAPI.sendEvent(StarEvent(SharedManager.UPDATE_NAME, payload))
                    }

                    if (updateAddress) {
                        BukkitManager.profileModule.updateProfile(profile)
                    }
                }
            }

            profile.name = e.name

            val grants = SharedManager.grantModule.findAllByPlayer(e.uniqueId)
            if (grants.isEmpty()) {
                SharedManager.grantModule.grant(SharedManager.rankModule.defaultRank, profile.id, ProfileModule.CONSOLE_ID, "Initial Rank", 0L)
            }

            SharedManager.grantModule.active[profile.id] = ArrayList()
            SharedManager.grantModule.active[profile.id]!!.addAll(grants.filter { it.isActive() })

            SharedManager.grantModule.setGrant(e.uniqueId, grants)
        }

        monitorPriority<PlayerLoginEvent> { e ->
            if (e.player.name.length > 16 || e.player.name.length < 3) {
                e.result = PlayerLoginEvent.Result.KICK_OTHER
                e.kickMessage = "${ChatColor.RED}You can't join the server with that name length."
                return@monitorPriority
            }

            val profile = BukkitManager.profileModule.getProfile(e.player.uniqueId)

            if (profile != null) {

                profile.name = e.player.name
                e.player.playerListName = ChatColor.valueOf(profile.chatColor).toString() + e.player.name
                profile.online = true
                profile.currentServer = Snowfall.get().config.getString("server-info.name")
                profile.lastJoined = System.currentTimeMillis()

                BukkitManager.profileModule.updateProfile(profile)
            } else {
                e.result = PlayerLoginEvent.Result.KICK_OTHER
                e.kickMessage = "${ChatColor.RED}We couldn't handle your profile, re-join, if the problem persists, contact the dev @rewir_ on discord."
                return@monitorPriority
            }

            e.allow()
        }


        monitorPriority<PlayerQuitEvent> { e ->
            val profile = BukkitManager.profileModule.getProfile(e.player.uniqueId)

            if (profile != null) {
                profile.lastServer = Snowfall.get().config.getString("server-info.name")
                profile.online = false
                profile.currentServer = "Not connected."
                profile.discordId = profile.discordId

                BukkitManager.profileModule.updateProfile(profile)
            }

            SharedManager.grantModule.active.remove(e.player.uniqueId)
        }

        highPriority<AsyncPlayerChatEvent> { e ->
            if (e.isCancelled) return@highPriority

            val player = e.player
            val profile = BukkitManager.profileModule.getProfile(e.player.uniqueId)
            val color = try {
                ChatColor.valueOf(profile?.chatColor ?: "WHITE")
            } catch (ex: Exception) {
                ChatColor.WHITE
            }

            if (!BukkitManager.configModule.mainConfig.config.getBoolean("CHAT-FORMAT.ENABLED")) return@highPriority

            val messageFormat: String

            val bestGrant = SharedManager.grantModule.findBestRank(SharedManager.grantModule.findAllByPlayer(player.uniqueId))
            val prefix = bestGrant.prefix ?: ""
            messageFormat = BukkitManager.configModule.mainConfig.config.getString("CHAT-FORMAT.FORMAT")
                .replace("{rank}", CC.translate(prefix))
                .replace("{name}",
                    CC.translate("${ChatColor.getLastColors(prefix) ?: ChatColor.WHITE}${player.name}") + "${ChatColor.RESET}")
                .replace("{message}", "$color${e.message}")

            e.isCancelled = true

            e.recipients.forEach { xd -> xd.sendMessage(messageFormat) }
        }
    }

}