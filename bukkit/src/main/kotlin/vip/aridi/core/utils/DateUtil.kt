package vip.aridi.core.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 22 - nov
 */

object DateUtil {

    private val timePattern = Pattern.compile(
        "(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?",
        2
    )

    @JvmStatic
    fun removeTimePattern(input: String): String {
        return timePattern.matcher(input).replaceFirst("").trim { it <= ' ' }
    }

    @JvmStatic
    fun parseDateDiff(time: String, future: Boolean): Long {
        val m = timePattern.matcher(time)
        var years = 0
        var months = 0
        var weeks = 0
        var days = 0
        var hours = 0
        var minutes = 0
        var seconds = 0
        var found = false

        while (m.find()) {
            if (m.group() != null && m.group().isNotEmpty()) {
                for (c in 0 until m.groupCount()) {
                    if (m.group(c) != null && m.group(c).isNotEmpty()) {
                        found = true
                        break
                    }
                }

                if (found) {
                    if (m.group(1) != null && m.group(1).isNotEmpty()) {
                        years = Integer.parseInt(m.group(1))
                    }

                    if (m.group(2) != null && m.group(2).isNotEmpty()) {
                        months = Integer.parseInt(m.group(2))
                    }

                    if (m.group(3) != null && m.group(3).isNotEmpty()) {
                        weeks = Integer.parseInt(m.group(3))
                    }

                    if (m.group(4) != null && m.group(4).isNotEmpty()) {
                        days = Integer.parseInt(m.group(4))
                    }

                    if (m.group(5) != null && m.group(5).isNotEmpty()) {
                        hours = Integer.parseInt(m.group(5))
                    }

                    if (m.group(6) != null && m.group(6).isNotEmpty()) {
                        minutes = Integer.parseInt(m.group(6))
                    }

                    if (m.group(7) != null && m.group(7).isNotEmpty()) {
                        seconds = Integer.parseInt(m.group(7))
                    }
                    break
                }
            }
        }

        if (!found) {
            throw Exception("Illegal Date")
        } else {
            val calendar = GregorianCalendar()

            val tense = if (future) {
                1
            } else {
                -1
            }

            if (years > 0) {
                calendar.add(1, years * tense)
            }

            if (months > 0) {
                calendar.add(2, months * tense)
            }

            if (weeks > 0) {
                calendar.add(3, weeks * tense)
            }

            if (days > 0) {
                calendar.add(5, days * tense)
            }

            if (hours > 0) {
                calendar.add(11, hours * tense)
            }

            if (minutes > 0) {
                calendar.add(12, minutes * tense)
            }

            if (seconds > 0) {
                calendar.add(13, seconds * tense)
            }

            val max = GregorianCalendar()
            max.add(1, 2)

            return if (calendar.after(max)) {
                max.timeInMillis
            } else {
                calendar.timeInMillis
            }
        }
    }

    @JvmStatic
    fun parseDuration(time: String): Long {
        val m = timePattern.matcher(time)
        var years = 0
        var months = 0
        var weeks = 0
        var days = 0
        var hours = 0
        var minutes = 0
        var seconds = 0
        var found = false

        while (m.find()) {
            if (m.group() != null && m.group().isNotEmpty()) {
                for (c in 0 until m.groupCount()) {
                    if (m.group(c) != null && m.group(c).isNotEmpty()) {
                        found = true
                        break
                    }
                }

                if (found) {
                    if (m.group(1) != null && m.group(1).isNotEmpty()) {
                        years = Integer.parseInt(m.group(1))
                    }

                    if (m.group(2) != null && m.group(2).isNotEmpty()) {
                        months = Integer.parseInt(m.group(2))
                    }

                    if (m.group(3) != null && m.group(3).isNotEmpty()) {
                        weeks = Integer.parseInt(m.group(3))
                    }

                    if (m.group(4) != null && m.group(4).isNotEmpty()) {
                        days = Integer.parseInt(m.group(4))
                    }

                    if (m.group(5) != null && m.group(5).isNotEmpty()) {
                        hours = Integer.parseInt(m.group(5))
                    }

                    if (m.group(6) != null && m.group(6).isNotEmpty()) {
                        minutes = Integer.parseInt(m.group(6))
                    }

                    if (m.group(7) != null && m.group(7).isNotEmpty()) {
                        seconds = Integer.parseInt(m.group(7))
                    }
                    break
                }
            }
        }

        if (!found) {
            throw Exception("Illegal Date")
        } else {
            val calendar = GregorianCalendar()
            val start = calendar.timeInMillis

            if (years > 0) {
                calendar.add(1, years)
            }

            if (months > 0) {
                calendar.add(2, months)
            }

            if (weeks > 0) {
                calendar.add(3, weeks)
            }

            if (days > 0) {
                calendar.add(5, days)
            }

            if (hours > 0) {
                calendar.add(11, hours)
            }

            if (minutes > 0) {
                calendar.add(12, minutes)
            }

            if (seconds > 0) {
                calendar.add(13, seconds)
            }

            val max = GregorianCalendar()
            max.add(1, 2)

            return if (calendar.after(max)) {
                max.timeInMillis - start
            } else {
                calendar.timeInMillis - start
            }
        }
    }

    @JvmStatic
    internal fun dateDiff(type: Int, fromDate: Calendar, toDate: Calendar, future: Boolean): Int {
        var diff = 0

        var savedDate: Long
        savedDate = fromDate.timeInMillis
        while (future && !fromDate.after(toDate) || !future && !fromDate.before(toDate)) {
            savedDate = fromDate.timeInMillis
            fromDate.add(type, if (future) 1 else -1)
            ++diff
        }

        --diff
        fromDate.timeInMillis = savedDate
        return diff
    }

    @JvmStatic
    fun formatDateDiff(date: Long): String {
        val c = GregorianCalendar()
        c.timeInMillis = date
        val now = GregorianCalendar()
        return formatDateDiff(now, c)
    }

    @JvmStatic
    fun formatDateDiff(fromDate: Calendar, toDate: Calendar): String {
        var future = false
        if (toDate == fromDate) {
            return "now"
        } else {
            if (toDate.after(fromDate)) {
                future = true
            }

            val sb = StringBuilder()
            val types = intArrayOf(1, 2, 5, 11, 12, 13)
            val names = arrayOf(
                "year",
                "years",
                "month",
                "months",
                "day",
                "days",
                "hour",
                "hours",
                "minute",
                "minutes",
                "second",
                "seconds"
            )
            var accuracy = 0

            var i = 0
            while (i < types.size && accuracy <= 2) {
                val diff = dateDiff(
                    types[i],
                    fromDate,
                    toDate,
                    future
                )
                if (diff > 0) {
                    ++accuracy
                    sb.append(" ").append(diff).append(" ").append(names[i * 2 + (if (diff > 1) 1 else 0)])
                }
                ++i
            }

            return if (sb.length == 0) "now" else sb.toString().trim { it <= ' ' }
        }
    }

    @JvmStatic
    fun formatSimplifiedDateDiff(date: Long): String {
        val c = GregorianCalendar()
        c.timeInMillis = date
        val now = GregorianCalendar()
        return formatSimplifiedDateDiff(now, c)
    }

    @JvmStatic
    fun formatSimplifiedDateDiff(fromDate: Calendar, toDate: Calendar): String {
        var future = false
        if (toDate == fromDate) {
            return "now"
        } else {
            if (toDate.after(fromDate)) {
                future = true
            }

            val sb = StringBuilder()
            val types = intArrayOf(1, 2, 5, 11, 12, 13)
            val names = arrayOf("y", "y", "m", "m", "d", "d", "h", "h", "m", "m", "s", "s")
            var accuracy = 0

            var i = 0
            while (i < types.size && accuracy <= 2) {
                val diff = dateDiff(
                    types[i],
                    fromDate,
                    toDate,
                    future
                )
                if (diff > 0) {
                    ++accuracy
                    sb.append(" ").append(diff).append("").append(names[i * 2 + (if (diff > 1) 1 else 0)])
                }
                ++i
            }

            return if (sb.length == 0) "now" else sb.toString().trim { it <= ' ' }
        }
    }

    @JvmStatic
    fun readableTime(time: Long): String {
        val second: Short = 1000
        val minutes = 60 * second
        val hours = 60 * minutes
        val days = 24 * hours
        var ms = time
        val text = StringBuilder("")

        if (time > days.toLong()) {
            text.append(time / days.toLong()).append(" days ")
            ms = time % days.toLong()
        }

        if (ms > hours.toLong()) {
            text.append(ms / hours.toLong()).append(" hours ")
            ms %= hours.toLong()
        }

        if (ms > minutes.toLong()) {
            text.append(ms / minutes.toLong()).append(" minutes ")
            ms %= minutes.toLong()
        }

        if (ms > second.toLong()) {
            text.append(ms / second.toLong()).append(" seconds ")
            val var10000 = ms % second.toLong()
        }

        return text.toString()
    }

    @JvmStatic
    fun readableTime(time: BigDecimal): String? {
        val text = ""

        if (time.toDouble() <= 60) {
            time.add(BigDecimal.valueOf(0.1))
            return text + " " + time + "s"
        } else if (time.toDouble() <= 3600) {
            val minutes = time.toInt() / 60
            val seconds = time.toInt() % 60
            val formatter = DecimalFormat("00")
            return text + " " + formatter.format(minutes.toLong()) + ":" + formatter.format(seconds.toLong()) + "m"
        }

        return null
    }

}