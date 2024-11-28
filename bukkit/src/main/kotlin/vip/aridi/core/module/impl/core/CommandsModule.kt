package vip.aridi.core.module.impl.core

import com.jonahseguin.drink.CommandService
import com.jonahseguin.drink.Drink
import vip.aridi.core.Snowfall
import vip.aridi.core.command.admin.grants.GrantCommand
import vip.aridi.core.command.admin.grants.GrantsCommand
import vip.aridi.core.command.admin.grants.oGrantCommand
import vip.aridi.core.command.admin.ranks.RankCommand
import vip.aridi.core.command.essentials.*
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.profile.Profile
import vip.aridi.core.provider.DurationProvider
import vip.aridi.core.provider.ProfileProvider
import vip.aridi.core.provider.RankProvider
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

        drink.bind(Profile::class.java).toProvider(ProfileProvider())
        drink.bind(Rank::class.java).toProvider(RankProvider())
        drink.bind(Duration::class.java).toProvider(DurationProvider())

        val commands = mapOf(
            DiscordCommand() to listOf("discord", "dc"),
            TwitterCommand() to listOf("x", "twitter"),
            TeamSpeakCommand() to listOf("teamspeak", "ts"),
            WebsiteCommand() to listOf("website"),
            StoreCommand() to listOf("store"),
            GrantCommand() to listOf("grant"),
            GrantsCommand() to listOf("grants"),
            oGrantCommand() to listOf("ogrant"),
            RankCommand() to listOf("rank")
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