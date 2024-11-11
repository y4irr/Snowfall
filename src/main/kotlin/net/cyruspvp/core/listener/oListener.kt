package net.cyruspvp.core.listener

import net.cyruspvp.net.cyruspvp.core.Snowfall
import org.bukkit.event.*
import org.bukkit.plugin.java.JavaPlugin

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

abstract class oListener(plugin: JavaPlugin) : Listener {
    init {
        registerEvents()

        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    protected abstract fun registerEvents()

    protected inline fun <reified T : Event> monitorPriority(crossinline action: (T) -> Unit) {
        val listener = object : Listener {
            @EventHandler(priority = EventPriority.MONITOR)
            fun onEvent(event: Event) {
                if (event is T) {
                    action(event)
                }
            }
        }

        register(listener)
    }


    protected inline fun <reified T : Event> highestPriority(crossinline action: (T) -> Unit) {
        val listener = object : Listener {
            @EventHandler(priority = EventPriority.HIGHEST)
            fun onEvent(event: Event) {
                if (event is T) {
                    action(event)
                }
            }
        }

        register(listener)
    }

    protected inline fun <reified T : Event> highPriority(crossinline action: (T) -> Unit) {
        val listener = object : Listener {
            @EventHandler(priority = EventPriority.HIGH)
            fun onEvent(event: Event) {
                if (event is T) {
                    action(event)
                }
            }
        }

        register(listener)
    }

    protected inline fun <reified T : Event> normalPriority(crossinline action: (T) -> Unit) {
        val listener = object : Listener {
            @EventHandler(priority = EventPriority.NORMAL)
            fun onEvent(event: Event) {
                if (event is T) {
                    action(event)
                }
            }
        }
        register(listener)
    }

    protected inline fun <reified T : Event> lowPriority(crossinline action: (T) -> Unit) {
        val listener = object : Listener {
            @EventHandler(priority = EventPriority.LOW)
            fun onEvent(event: Event) {
                if (event is T) {
                    action(event)
                }
            }
        }

        register(listener)
    }

    fun register(listener: Listener) {
        HandlerList.getRegisteredListeners(Snowfall.get()).forEach { handler ->
            handler.plugin?.let {
                if (it is JavaPlugin) {
                    it.server.pluginManager.registerEvents(listener, it)
                }
            }
        }
    }
}