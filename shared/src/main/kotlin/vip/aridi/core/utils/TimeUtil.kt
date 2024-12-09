package vip.aridi.core.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.round

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

object TimeUtil {

    @JvmStatic val HOUR = TimeUnit.HOURS.toMillis(60L)
    @JvmStatic val MINUTE = TimeUnit.SECONDS.toMillis(60L)
    @JvmStatic val DATE_FORMAT = SimpleDateFormat("MM/dd/yyyy HH:mm")
    @JvmStatic  val dateFormatNoTime = SimpleDateFormat("MM/dd/yyyy")

    @JvmStatic
    fun formatIntoFancy(millis: Long):String {

        if (millis >= MINUTE) {
            return formatIntoMMSS(millis)
        }

        val seconds = millis / 1000.0

        return "${if (seconds > 0.1) round(10.0 * seconds) / 10.0 else 0.1}s"
    }

    @JvmStatic
    fun formatTime(milliseconds: Long): String {
        val df = DecimalFormat("#0.00")
        return df.format(milliseconds / 1000.0)
    }

    @JvmStatic
    fun formatIntoHHMMSS(millis: Long): String {

        var toReturn = (millis/1000)

        val seconds = toReturn % 60

        toReturn -= seconds

        var minutesCount = (toReturn / 60)
        val minutes = minutesCount % 60

        minutesCount -= minutes

        val hours = minutesCount / 60



        return ((if (hours < 10) "0" else "") + hours + ":") + (if (minutes < 10) "0" else "") + minutes + ":" + (if (seconds < 10) "0" else "") + seconds
    }

    @JvmStatic
    fun formatIntoCalendarStringNoTime(date: Date?): String {
        return dateFormatNoTime.format(date)
    }

    @JvmStatic
    fun getTimeZoneShortName(displayName: String): String {
        val stz = displayName.split(" ")
        val sName = StringBuilder()

        for (s in stz) {
            sName.append(s[0])
        }
        return sName.toString()
    }

    private val MM_SS_BUILDER: ThreadLocal<StringBuilder> = ThreadLocal.withInitial { java.lang.StringBuilder() }

    @JvmStatic
    fun formatIntoMMSS(secs: Int): String {
        var secs = secs
        val seconds = secs % 60
        secs -= seconds

        var minutesCount = (secs / 60).toLong()
        val minutes = minutesCount % 60L
        minutesCount -= minutes

        val hours = minutesCount / 60L

        val result = MM_SS_BUILDER.get()
        result.setLength(0)

        if (hours > 0L) {
            if (hours < 10L) {
                result.append("0")
            }

            result.append(hours)
            result.append(":")
        }

        if (minutes < 10L) {
            result.append("0")
        }

        result.append(minutes)
        result.append(":")

        if (seconds < 10) {
            result.append("0")
        }

        result.append(seconds)

        return result.toString()
    }

    @JvmStatic
    fun formatIntoMMSS(millis: Long): String {

        var toReturn = (millis/1000)

        val seconds = toReturn % 60

        toReturn -= seconds

        var minutesCount = (toReturn / 60)
        val minutes = minutesCount % 60

        minutesCount -= minutes

        val hours = minutesCount / 60

        return (if (hours > 0) (if (hours < 10) "0" else "") + hours + ":" else "") + (if (minutes < 10) "0" else "") + minutes + ":" + (if (seconds < 10) "0" else "") + seconds
    }

    @JvmStatic
    fun formatIntoDetailedString(millis: Long): String {

        if (millis <= 0L) {
            return "0 seconds"
        }

        val secs = millis / 1000L

        val remainder = secs % 86400

        val days = secs / 86400
        val hours = remainder / 3600
        val minutes = remainder / 60 - hours * 60
        val seconds = remainder % 3600 - minutes * 60

        val fDays = if (days > 0) " " + days + " day" + (if (days > 1) "s" else "") else ""

        var fHours = ""
        var fMinutes = ""
        var fSeconds = ""

        if (days < 1) {
            fHours = if (hours > 0) " " + hours + " hour" + (if (hours > 1) "s" else "") else ""

            if (hours < 1) {
                fMinutes = if (minutes > 0) " " + minutes + " minute" + (if (minutes > 1) "s" else "") else ""
                fSeconds = if (seconds > 0) " " + seconds + " second" + (if (seconds > 1) "s" else "") else ""
            }

        }

        return (fDays + fHours + fMinutes + fSeconds).trim { it <= ' ' }
    }

    @JvmStatic
    fun formatIntoShortString(millis: Long,suppress: Boolean = true):String {

        if (millis <= 1000L) {
            return "0s"
        }

        val secs = millis / 1000L

        if (secs < 60) {
            return "${secs}s"
        }

        val remainder = secs % 86400

        val days = secs / 86400
        val hours = remainder / 3600
        val minutes = remainder / 60 - hours * 60
        val seconds = remainder % 3600 - minutes * 60

        val fDays = if (days > 0) " ${days}d" else ""

        var fHours = ""
        var fMinutes = ""
        var fSeconds = ""

        if (days < 1) {
            fHours = if (hours > 0) " ${hours}h" else ""

            if (!suppress) {

                if (minutes > 0) {
                    fMinutes = "${minutes}m"
                } else if (seconds > 0) {
                    fSeconds = "${seconds}s"
                }

            }

            if (hours < 1) {

                if (minutes > 0) {
                    fMinutes = " ${minutes}m"
                }

                if (!suppress && seconds > 0) {
                    fSeconds = " ${seconds}s"
                }

            }

        }

        return (fDays + fHours + fMinutes + fSeconds).trim { it <= ' ' }
    }

    @JvmStatic
    fun formatIntoCalendarString(date: Date): String {
        return DATE_FORMAT.format(date)
    }

    @JvmStatic
    fun parseTime(time: String): Long {

        if (time.isEmpty() || time.startsWith("perm",true)) {
            return 0L
        }

        val lifeMatch = arrayOf("y","M","w", "d", "h", "m", "s")
        val lifeInterval = intArrayOf(31536000,2592000,604800,86400,3600,60,1)

        var seconds = 0

        for (i in lifeMatch.indices) {

            val matcher = Pattern.compile("([0-9]*)" + lifeMatch[i]).matcher(time)

            while (matcher.find()) {
                seconds += Integer.parseInt(matcher.group(1)) * lifeInterval[i]
            }

        }

        return seconds * 1000L
    }

    @JvmStatic
    fun getTimeBetween(a: Date, b: Date): Long {
        return abs(a.time - b.time)
    }

}