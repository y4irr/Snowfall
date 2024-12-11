package vip.aridi.core.grant

import vip.aridi.core.SnowfallProxy
import vip.aridi.core.module.ProxyManager
import vip.aridi.core.module.system.GrantModule
import java.util.*

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ SnowfallProxy.instance
 * @author YairSoto © 2024
 * Date: 09 - dic
 */

class GrantProxyAdapter : GrantModule.GrantAdapter {
    override fun onGrantApply(uuid: UUID, grant: Grant) {
        SnowfallProxy.instance.proxy.getPlayer(uuid).also { ProxyManager.permissionModule.update(it) }
    }

    override fun onGrantChange(uuid: UUID,grant: Grant) {
        SnowfallProxy.instance.proxy.getPlayer(uuid).also{ ProxyManager.permissionModule.update(it) }
    }

    override fun onGrantExpire(uuid: UUID,grant: Grant) {
        SnowfallProxy.instance.proxy.getPlayer(uuid).also{ ProxyManager.permissionModule.update(it) }
    }

    override fun onGrantRemove(uuid: UUID,grant: Grant) {
        SnowfallProxy.instance.proxy.getPlayer(uuid).also{ ProxyManager.permissionModule.update(it) }
    }
}