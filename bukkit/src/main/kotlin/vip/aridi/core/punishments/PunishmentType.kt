package vip.aridi.core.punishments

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 24 - nov
 */

enum class PunishmentType(
    val kickOnExecute: Boolean,
    val context: String,
    val color: String,
    val woolData: Int
) {
    KICK(true, "kicked", "§a", 5),
    WARN(false, "warned", "§e", 4),
    MUTE(false, "muted", "§6", 1),
    BAN(true, "banned", "§c", 14),
    BLACKLIST(true, "blacklisted", "§4", 0);

    fun permission(pardon: Boolean, isIP: Boolean = false): String {
        return "core.punishment.${if (isIP) "ip" else ""}${name.lowercase()}${if (pardon) ".pardon" else ""}"
    }
}