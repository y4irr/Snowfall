package vip.aridi.core.module.impl.system

import vip.aridi.core.module.IModule
import vip.aridi.core.module.ModuleCategory
import vip.aridi.core.punishments.Punishment
import vip.aridi.core.punishments.PunishmentReason
import vip.aridi.core.punishments.PunishmentType
import java.util.UUID

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 24 - nov
 */

class PunishmentModule: IModule {
    private val punishment = Punishment()

    override fun order(): Int = 6

    override fun category(): ModuleCategory = ModuleCategory.SYSTEM

    override fun load() {}

    override fun unload() {}

    override fun reload() {

    }

    override fun moduleName(): String = "Punishments"

    fun isMuted(uuid: UUID): Boolean {
        return false
    }

    fun punish(
        type: PunishmentType,
        victim: UUID,
        sender: UUID,
        reason: PunishmentReason,
        server: String,
        duration: Long = 0L,
        silent: Boolean,
        victimDisplay: String,
        senderDisplay: String
    ): Boolean {
        return false
    }

    fun pardon(
        punishmentId: UUID,
        pardoner: UUID,
        reason: String,
        silent: Boolean,
        victimDisplay: String,
        senderDisplay: String
    ): Boolean {
        return false
    }
}