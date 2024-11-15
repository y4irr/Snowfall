package vip.aridi.core.module

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

interface IModule {

    fun order(): Int

    fun category(): ModuleCategory

    fun load()

    fun unload()

    fun reload()

    fun moduleName(): String
}