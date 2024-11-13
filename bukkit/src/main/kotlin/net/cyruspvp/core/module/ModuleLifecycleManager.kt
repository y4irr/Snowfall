package net.cyruspvp.core.module

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

interface ModuleLifecycleManager {

    fun startup()

    fun enabled()

    fun disabled()

    fun loadModules()

    fun unloadModules()

    fun addModules(iModule: IModule)

    fun reloadModule(moduleName: Int)

}