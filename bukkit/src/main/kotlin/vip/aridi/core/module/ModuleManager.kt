package vip.aridi.core.module

import vip.aridi.core.utils.CC
import vip.aridi.core.Snowfall
import org.bukkit.command.ConsoleCommandSender
import vip.aridi.core.module.impl.core.CommandsModule
import vip.aridi.core.module.impl.core.ConfigurationModule
import vip.aridi.core.module.impl.core.DatabaseModule
import vip.aridi.core.module.impl.core.ProfileModule
import vip.aridi.core.module.impl.system.*

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

class ModuleManager(plugin: Snowfall):ModuleLifecycleManager {
    private val modules: MutableList<IModule> = mutableListOf()
    private val console: ConsoleCommandSender = plugin.server.consoleSender

    companion object {
        @JvmStatic
        val configModule = ConfigurationModule()
        val databaseModule = DatabaseModule()
        val profileModule = ProfileModule()
        val rankModule = RankModule()
        val grantModule = GrantModule()
        val permissionModule = PermissionModule()
    }
    private val commandsModule = CommandsModule()
    private val listenerModule = ListenersModule()
    private val managerModule = ManagerModule()


    init {
        startup()
        console.sendMessage(CC.translate("&7[&bSnowfall&7] &aModule startup task finished successfully"))
    }

    /**t
     * I Already know use enabled and disabled
     * when there's a startup method is useless
     * but when you obfuscate all this looks very different
     * that's why I decided to add one more useless method :p
     *
     * Yair Soto - 2024
     */

    override fun startup() {
        console.sendMessage(CC.translate("&7[&bSnowfall&7] &aModule startup task has been initialized"))
        initModules()
        enabled()
    }

    private fun initModules() {
        addModules(configModule)
        addModules(databaseModule)
        addModules(profileModule)
        addModules(listenerModule)
        addModules(rankModule)
        addModules(grantModule)
        addModules(permissionModule)
        addModules(managerModule)
        addModules(commandsModule)
    }

    override fun enabled() {
        loadModules()
    }

    override fun disabled() {
        unloadModules()
    }

    override fun loadModules() {
        modules.sortedWith(compareBy(
            { it.category().order },
            { it.order() }
        )).forEach {
            it.load()
            console.sendMessage(CC.translate("&7[&bModule System&7] &a${it.moduleName()} module has been enabled successfully"))
        }
    }


    override fun unloadModules() {
        modules.sortedByDescending {
            it.category().order
            it.order() }.forEach {
            it.unload()
            console.sendMessage(CC.translate("&7[&bModule System&7] &c${it.moduleName()} module has been disabled successfully"))
        }
    }

    override fun addModules(iModule: IModule) {
        modules.add(iModule)
    }

    override fun reloadModule(moduleName: Int) {
        modules.firstOrNull { it.order() == moduleName }?.reload()
    }
}