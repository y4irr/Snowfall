package vip.aridi.core.module

import vip.aridi.core.module.impl.core.CommandsModule
import vip.aridi.core.module.impl.core.ConfigurationModule
import vip.aridi.core.module.impl.core.ProfileModule
import vip.aridi.core.module.impl.system.*
import vip.aridi.core.module.system.PunishmentModule
import java.util.logging.Logger

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

class BukkitManager: ModuleLifecycleManager {
    private val logger = Logger.getLogger(SharedManager::class.java.name)

    companion object {
        @JvmStatic
        val configModule = ConfigurationModule()
        val profileModule = ProfileModule()
        val permissionModule = PermissionModule()
    }
    private val commandsModule = CommandsModule()
    private val listenerModule = ListenersModule()
    private val managerModule = ManagerModule()


    init {
        startup()
        logger.info("[Bukkit Module] Module startup task finished successfully")
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
        logger.info("[Bukkit Module] Module startup task has been initialized")
        initModules()
        enabled()
    }

    private fun initModules() {

        addModules(configModule)
        addModules(SharedManager.databaseModule)
        addModules(profileModule)
        addModules(listenerModule)
        addModules(SharedManager.rankModule)
        addModules(SharedManager.grantModule)
        addModules(SharedManager.punishmentModule)
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
        SharedManager.modules.sortedWith(compareBy(
            { it.category().order },
            { it.order() }
        )).forEach {
            it.load()

            logger.info("[Module System] ${it.moduleName()} from bukkit loaded successfully")
        }
    }


    override fun unloadModules() {
        SharedManager.modules.sortedByDescending {
            it.category().order
            it.order() }.forEach {
            it.unload()
            logger.info("[Module System] ${it.moduleName()} module has been disabled successfully")
        }
    }

    override fun addModules(iModule: IModule) {
        SharedManager.modules.add(iModule)
    }

    override fun reloadModule(moduleName: Int) {
        SharedManager.modules.firstOrNull { it.order() == moduleName }?.reload()
    }
}