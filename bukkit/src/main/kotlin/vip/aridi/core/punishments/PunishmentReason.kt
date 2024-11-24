package vip.aridi.core.punishments

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 24 - nov
 */

enum class PunishmentReason(
    val message: String,
    val category: String) {
    SPAM("Spamming chat", "Chat Misconduct"),
    CHEATING("Using unauthorized mods", "Game Misconduct"),
    OFFENSIVE_LANGUAGE("Using offensive language", "Chat Misconduct"),
    EXPLOITING("Exploiting game bugs", "Game Misconduct"),
    OTHER("Breaking the rules", "General");

    companion object {
        fun fromMessage(message: String): PunishmentReason {
            return entries.firstOrNull { it.message.equals(message, ignoreCase = true) } ?: OTHER
        }
    }
}