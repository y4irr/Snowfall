package vip.aridi.core.provider

import com.jonahseguin.drink.argument.CommandArg
import com.jonahseguin.drink.exception.CommandExitMessage
import com.jonahseguin.drink.parametric.DrinkProvider
import vip.aridi.core.rank.Rank
import vip.aridi.core.module.system.RankModule
import vip.aridi.core.module.BukkitManager
import vip.aridi.core.module.SharedManager

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 23 - nov
 */

class RankProvider : DrinkProvider<Rank>() {

    private val rankModule: RankModule = SharedManager.rankModule

    override fun doesConsumeArgument(): Boolean = true

    override fun isAsync(): Boolean = false

    override fun allowNullArgument(): Boolean = false

    override fun provide(arg: CommandArg, annotations: MutableList<out Annotation>): Rank {
        val rankName = arg.get()

        val rank = rankModule.getRankById(rankName)
            ?: throw CommandExitMessage("$rankName was not found")

        return rank
    }

    override fun argumentDescription(): String = "Rank Name"

    override fun getSuggestions(prefix: String): List<String> {
        return rankModule.getAllRanks()
            .filter { it.name.startsWith(prefix, ignoreCase = true) }
            .map { it.name }
    }
}