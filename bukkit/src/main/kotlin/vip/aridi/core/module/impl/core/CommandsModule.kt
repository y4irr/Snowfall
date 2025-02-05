package vip.aridi.core.module.impl.core

import com.jonahseguin.drink.CommandService
import com.jonahseguin.drink.Drink
import com.jonahseguin.drink.parametric.DrinkProvider
import org.bukkit.GameMode
import org.bukkit.command.defaults.EnchantCommand
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import vip.aridi.core.Snowfall
import vip.aridi.core.command.admin.grants.*
import vip.aridi.core.command.admin.ranks.*
import vip.aridi.core.command.essentials.*
import vip.aridi.core.command.staff.punishment.*
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.profile.Profile
import vip.aridi.core.provider.*
import vip.aridi.core.rank.Rank
import vip.aridi.core.utils.Duration

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class CommandsModule: IModule {
    override fun order(): Int = 4
    override fun category(): ModuleCategory = ModuleCategory.CORE

    override fun load() {
        val drink: CommandService = Drink.get(Snowfall.get())

        fun <T : Any> bind(type: Class<T>, provider: DrinkProvider<T>) {
            drink.bind(type).toProvider(provider)
        }

        bind(Profile::class.java, ProfileProvider())
        bind(Rank::class.java, RankProvider())
        bind(Duration::class.java, DurationProvider())
        bind(GameMode::class.java, GamemodeProvider())
        bind(Enchantment::class.java, EnchantmentProvider())
        bind(Player::class.java, PlayerProvider())

        val commands = mapOf(

            //Essentials
            DiscordCommand() to listOf("discord", "dc"),
            TwitterCommand() to listOf("x", "twitter"),
            TeamSpeakCommand() to listOf("teamspeak", "ts"),
            WebsiteCommand() to listOf("website"),
            StoreCommand() to listOf("store"),
            LoreCommand() to listOf("lore"),
            ListCommand() to listOf("list"),
            SudoCommand() to listOf("sudo"),
            HatCommand() to listOf("hat"),
            EnchantCommand() to listOf("enchant"),
            FeedCommand() to listOf("feed"),
            GamemodeCommand() to listOf("gamemode"),
            GiveCommand() to listOf("give"),
            HeadCommand() to listOf("head", "skull"),
            KillAllCommand() to listOf("killall"),
            KillCommand() to listOf("kill"),
            ListCommand() to listOf("list"),
            LoreCommand() to listOf("lore"),
            MoreCommand() to listOf("more"),
            PingCommand() to listOf("ping"),
            PlayTimeCommand() to listOf("playtime"),
            RawCommand() to listOf("raw"),
            RenameCommand() to listOf("rename"),
            RepairCommand() to listOf("repair"),
            ReportCommand() to listOf("report"),
            SpeedCommand() to listOf("speed"),
            StoreCommand() to listOf("store"),
            SuicideCommand() to listOf("suicide"),
            TeleportationCommand() to listOf("tp"),

            //Rank commands
            GrantCommand() to listOf("grant"),
            GrantsCommand() to listOf("grants"),
            oGrantCommand() to listOf("ogrant"),
            RankCommand() to listOf("rank"),

            //Punishments commands
            BlacklistCommand() to listOf("blacklist"),
            BanCommand() to listOf("ban"),
            MuteCommand() to listOf("mute"),
            IPBanCommand() to listOf("ipban"),
            IPMuteCommand() to listOf("ipmute"),
            KickCommand() to listOf("kick"),
            UnBanCommand() to listOf("unban"),
            UnBlacklistCommand() to listOf("unblacklist"),
            UnMuteCommand() to listOf("unmute"),
        )

        commands.forEach { (commands, aliases) ->
            aliases.forEach { alias ->
                drink.register(commands, alias)
            }
        }

        drink.registerCommands()
    }

    override fun unload() {

    }

    override fun reload() {

    }

    override fun moduleName(): String {
        return "Configuration"
    }
}