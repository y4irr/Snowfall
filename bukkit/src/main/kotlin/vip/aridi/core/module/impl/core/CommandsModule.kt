package vip.aridi.core.module.impl.core

import com.jonahseguin.drink.CommandService
import com.jonahseguin.drink.Drink
import vip.aridi.core.Snowfall
import vip.aridi.core.command.*
import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 13 - nov
 */

class CommandsModule: IModule {
    override fun order(): Int {
        return 3
    }

    override fun category(): ModuleCategory {
        return ModuleCategory.CORE
    }

    override fun load() {
        val drink: CommandService = Drink.get(Snowfall.get())

        val commands = mapOf(
            DiscordCommand() to listOf("discord", "dc"),
            TwitterCommand() to listOf("x", "twitter"),
            TeamSpeakCommand() to listOf("teamspeak", "ts"),
            WebsiteCommand() to listOf("website"),
            StoreCommand() to listOf("store"),
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