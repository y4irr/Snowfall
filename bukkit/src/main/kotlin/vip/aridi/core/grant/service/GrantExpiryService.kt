package vip.aridi.core.grant.service

import vip.aridi.core.grant.Grant
import vip.aridi.core.module.ModuleManager
import java.util.UUID

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 15 - nov
 */

class GrantExpiryService: Runnable {
    private val module = ModuleManager.grantModule

    override fun run() {
        val expiredGrants = mutableListOf<Pair<UUID, Grant>>()

        module.active.entries.removeIf { (uuid, grants) ->
            val validGrants = grants.filterNot { grant ->
                val isExpired = grant.isVoided() || grant.isRemoved()
                if (isExpired) {
                    expiredGrants.add(uuid to grant)
                }
                isExpired
            }
            if (validGrants.isEmpty()) {
                true
            } else {
                grants.clear()
                grants.addAll(validGrants)
                module.setGrant(uuid, validGrants)
                false
            }
        }

        expiredGrants.forEach { (uuid, grants) ->
            module.findProvider().ifPresent { it.onGrantExpire(uuid, grants)}
        }
    }
}