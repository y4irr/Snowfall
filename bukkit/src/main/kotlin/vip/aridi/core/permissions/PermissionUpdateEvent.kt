package vip.aridi.core.permissions

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 19 - nov
 */

class PermissionUpdateEvent(val player: Player) : Event(){

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic val handlerList = HandlerList()
    }


}