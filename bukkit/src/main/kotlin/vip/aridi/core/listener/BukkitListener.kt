package vip.aridi.core.listener

import com.google.gson.JsonObject
import com.mongodb.client.model.Filters
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
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
import vip.aridi.core.module.system.GrantModule
import vip.aridi.core.profile.Profile
import vip.aridi.core.punishments.PunishmentType
import vip.aridi.core.star.StarPunishmentListener
import vip.aridi.core.utils.CC
import vip.aridi.core.utils.TimeUtil
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

class BukkitListener : Listener {
    
    @EventHandler(priority = EventPriority.HIGH)
    fun onAsyncPlayerPreLoginEvent(e: AsyncPlayerPreLoginEvent) {
        if (e.name == null) return

        if (e.name.length > 16 || e.name.length < 3) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "${ChatColor.RED} Your name is too short or long for join, change your nickname")
            return
        }
        val player = Snowfall.get().server.getPlayer(e.uniqueId)

        if (player != null) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You logged in too fast or you're a bot, please re-join.")
            return
        }

        Bukkit.getScheduler().runTaskAsynchronously(Snowfall.get()) {
            try {
                val doc = SharedManager.databaseModule.getCollection("profiles")
                    .find(Filters.eq("_id", e.uniqueId.toString()))
                    .first()

                val profile = /*if (doc == null) */Profile(e.uniqueId, e.name)/* else {
                    try {
                        val jsonObject = SharedManager.databaseModule.gson.fromJson(doc.toJson(), JsonObject::class.java)
                        if (jsonObject.has("address") && jsonObject["address"].isJsonArray) {
                            jsonObject.remove("address")
                            jsonObject.addProperty("address", jsonObject["addresses"].asJsonArray.last().asString)
                        }
                        SharedManager.databaseModule.gson.fromJson(jsonObject.toString(), Profile::class.java)
                    } catch (ex: Exception) {
                        Bukkit.getLogger().severe("Profile JSON for ${e.name}: ${doc.toJson()}")
                        Bukkit.getLogger().severe("Error parsing profile for ${e.name}: ${ex.message}")
                        e.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
                        e.kickMessage = "${ChatColor.RED}We're having issues loading your profile, please try again later."
                        return@runTaskAsynchronously
                    }
                }*/

                val punishment = SharedManager.punishmentModule.findLastPunishment(e.uniqueId)

                if (punishment != null) {
                    if (punishment.type == PunishmentType.BAN) {
                        if (!BukkitManager.configModule.mainConfig.config.getBoolean("PUNISHMENT.BANNED-CAN-JOIN-HUB")) {
                            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, CC.translate("&cYou're currently banned from CyrusPvP\n &cReason: &f${punishment.reason}\n &eAppeal at: discord.gg/cyruspvp or ts.cyruspvp.net"))
                        }
                    }
                }

                val ip = e.address.hostAddress
                profile.online = true

                val updateName = profile.name != e.name || !BukkitManager.profileModule.isCached(e.uniqueId)
                val updateAddress = !profile.addresses.contains(ip)

                if (updateName || updateAddress) {
                    if (updateAddress) {
                        profile.address = ip
                        profile.addresses.add(ip)
                    }

                    Bukkit.getScheduler().runTaskAsynchronously(Snowfall.get()) {
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
                            BukkitManager.profileModule.toSave(profile)
                        }
                    }
                }

                profile.name = e.name

                val punishments = SharedManager.punishmentModule.findByVictimOrIdentifier(e.uniqueId, profile.addresses)
                if (punishments.isNotEmpty()) {
                    val punishment = SharedManager.punishmentModule.findMostRecentPunishment(punishments, arrayListOf(PunishmentType.BLACKLIST, PunishmentType.BAN))
                    if (punishment != null) {
                        e.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_BANNED
                        e.kickMessage = StarPunishmentListener.getPunishmentKickMessage(e.uniqueId, punishment, false)
                        return@runTaskAsynchronously
                    }
                }

                SharedManager.punishmentModule.mutes[profile.id] = punishments.filter { it.type == PunishmentType.MUTE }.toHashSet()
                BukkitManager.profileModule.profiles[profile.id] = profile

                val grants = SharedManager.grantModule.findAllByPlayer(e.uniqueId)
                if (grants.isEmpty()) {
                    SharedManager.grantModule.grant(
                        SharedManager.rankModule.defaultRank,
                        profile.id,
                        ProfileModule.CONSOLE_ID,
                        "Initial Rank",
                        0L
                    )
                }

                SharedManager.grantModule.active[profile.id] = ArrayList()
                SharedManager.grantModule.active[profile.id]!!.addAll(grants.filter { it.isActive() })

                SharedManager.grantModule.setGrant(e.uniqueId, grants)

            } catch (ex: Exception) {
                Bukkit.getLogger().severe("Unexpected error during AsyncPlayerPreLoginEvent for ${e.name}: ${ex.message}")
                e.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    "${ChatColor.RED}An error occurred, please try again later."
                )
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerLoginEvent(e: PlayerLoginEvent) {
        if (e.player.name.length > 16 || e.player.name.length < 3) {
            e.result = PlayerLoginEvent.Result.KICK_OTHER
            e.kickMessage = "${ChatColor.RED}You can't join the server with that name length."
            return
        }

        val profile = BukkitManager.profileModule.getProfile(e.player.uniqueId)

        if (profile != null) {

            profile.name = e.player.name
            e.player.playerListName = ChatColor.valueOf(profile.chatColor).toString() + e.player.name
            profile.online = true
            profile.currentServer = Snowfall.get().config.getString("server-info.name")
            profile.lastJoined = System.currentTimeMillis()

            BukkitManager.profileModule.updateProfile(profile)
            BukkitManager.profileModule.toSave(profile)
        } else {
            e.result = PlayerLoginEvent.Result.KICK_OTHER
            e.kickMessage = "${ChatColor.RED}We couldn't handle your profile, re-join, if the problem persists, contact the dev @rewir_ on discord."
            return
        }

        e.allow()
    }

    @EventHandler()
    fun onAsyncPlayerChat(e: AsyncPlayerChatEvent) {
        if (e.isCancelled) return

        val player = e.player
        val profile = BukkitManager.profileModule.getProfile(player.uniqueId)
        val color = try {
            ChatColor.valueOf(profile?.chatColor ?: "WHITE").toString()
        } catch (ex: Exception) {
            ChatColor.WHITE
        }

        val punishment = SharedManager.punishmentModule.findMostRecentPunishment(
            SharedManager.punishmentModule.mutes[player.uniqueId]!!,
            arrayListOf(PunishmentType.MUTE)
        )

        if (punishment != null) {
            val message = if (punishment.isPermanent()) {
                "You are permanently silenced"
            } else {
                "You are currently muted, you can speak again in ${ChatColor.YELLOW}${
                    TimeUtil.formatIntoDetailedString(
                        punishment.getRemaining()
                    )
                }${ChatColor.RED}."
            }
            player.sendMessage("${ChatColor.RED}$message")
            e.isCancelled = true
            return
        }

        if (!Snowfall.get().config.getBoolean("CHAT-FORMAT.ENABLED")) return

        val messageFormat: String

        val bestGrant = SharedManager.grantModule.findBestRank(SharedManager.grantModule.findAllByPlayer(player.uniqueId))
        val prefix = bestGrant.prefix ?: ""
        messageFormat = Snowfall.get().config.getString("CHAT-FORMAT.FORMAT")
            .replace("{rank}", CC.translate(prefix))
            .replace("{name}", CC.translate("${ChatColor.getLastColors(prefix) ?: ChatColor.WHITE}${player.name}") + "${ChatColor.RESET}: ")
            .replace("{message}", "$color${e.message}")
        e.isCancelled = true

        e.recipients.forEach { recipient ->
            recipient.sendMessage(messageFormat)
        }


    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerQuitEvent(e: PlayerQuitEvent) {
        val profile = BukkitManager.profileModule.getProfile(e.player.uniqueId)

        if (profile != null) {
            profile.lastServer = Snowfall.get().config.getString("server-info.name")
            profile.online = false
            profile.currentServer = "Not connected."
            profile.discordId = profile.discordId

            BukkitManager.profileModule.updateProfile(profile)
            BukkitManager.profileModule.toSave(profile)
        }

        SharedManager.grantModule.active.remove(e.player.uniqueId)
    }

    fun onAsyncPlayerChatEvent(e: AsyncPlayerChatEvent) {
        if (e.isCancelled) return

        val player = e.player
        val profile = BukkitManager.profileModule.getProfile(e.player.uniqueId)
        val color = try {
            ChatColor.valueOf(profile?.chatColor ?: "WHITE")
        } catch (ex: Exception) {
            ChatColor.WHITE
        }

        if (!BukkitManager.configModule.mainConfig.config.getBoolean("CHAT-FORMAT.ENABLED")) return

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