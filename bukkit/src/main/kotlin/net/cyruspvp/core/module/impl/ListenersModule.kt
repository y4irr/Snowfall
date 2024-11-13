package net.cyruspvp.core.module.impl

import net.cyruspvp.core.module.IModule
import net.cyruspvp.core.profile.listener.ProfileListener
import net.cyruspvp.net.cyruspvp.core.Snowfall

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 08 - nov
 */

class ListenersModule: IModule {
    override fun order(): Int {
        return 3
    }

    override fun load() {
        ProfileListener(Snowfall.get())
    }

    override fun unload() {

    }

    override fun reload() {

    }

    override fun moduleName(): String {
        return "Listener"
    }
}