package vip.aridi.core.provider

import com.jonahseguin.drink.argument.CommandArg
import com.jonahseguin.drink.exception.CommandExitMessage
import com.jonahseguin.drink.parametric.DrinkProvider
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import vip.aridi.core.module.ModuleManager
import vip.aridi.core.module.impl.core.ProfileModule
import vip.aridi.core.profile.Profile
import java.util.UUID

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 23 - nov
 */

class ProfileProvider : DrinkProvider<Profile>() {

    private val profileModule = ModuleManager.profileModule

    override fun doesConsumeArgument(): Boolean = true

    override fun isAsync(): Boolean = false

    override fun allowNullArgument(): Boolean = false

    override fun provide(arg: CommandArg, annotations: MutableList<out Annotation>): Profile {
        val targetName = arg.get()

        return profileModule.getProfile(targetName)
            ?: throw CommandExitMessage("$targetName's profile was not found.")
    }

    override fun argumentDescription(): String = "Player Profile"

    override fun getSuggestions(prefix: String): List<String> {
        return profileModule.getProfiles()
            .filter { it.name.startsWith(prefix, ignoreCase = true) }
            .map { it.name }
    }
}