package top.tsukino.mindly.feature.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    fun formatTime(time: String): Date? {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            formatter.parse(time.substring(0, 19))
        } catch (e: Exception) {
            null
        }
    }

    fun format(time: Date?): String? {
        return time?.let {
            val calendar = java.util.Calendar.getInstance()
            calendar.time = it
            val year = calendar.get(java.util.Calendar.YEAR)
            val nowYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            val pattern = if (nowYear == year) "MM月dd日 HH:mm:ss" else "yyyy年MM月dd日 HH:mm:ss"
            SimpleDateFormat(pattern, Locale.getDefault()).format(it)
        }
    }

    fun formatDate(time: Date?): String? {
        return time?.let {
            val calendar = java.util.Calendar.getInstance()
            calendar.time = it
            val year = calendar.get(java.util.Calendar.YEAR)
            val nowYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            val pattern = if (nowYear == year) "MM月dd日" else "yyyy年MM月dd日"
            SimpleDateFormat(pattern, Locale.getDefault()).format(it)
        }
    }

    fun formatReadableTime(time: Date?): String? {
        return time?.let {
            if (calculateTimeDiff(it) / 1000 / 60 / 60 / 24 > 7) {
                return format(it)
            }
            return formatTimeDiff(it)
        }
    }

    fun format(time: String): String? {
        return format(formatTime(time))
    }

    fun formatDate(time: String): String? {
        return formatDate(formatTime(time))
    }

    fun calculateTimeDiff(time: Date): Long {
        val now = Date()
        val diff = now.time - time.time
        return diff
    }

    fun formatTimeDiff(time: Date): String {
        val diff = calculateTimeDiff(time)
        val second = diff / 1000
        val minute = second / 60
        val hour = minute / 60
        val day = hour / 24
        val week = day / 7
        val month = day / 30
        val year = month / 12
        return when {
            year > 0 -> "${year}年前"
            month > 0 -> "${month}月前"
            week > 0 -> "${week}周前"
            day > 0 -> "${day}天前"
            hour > 0 -> "${hour}小时前"
            minute > 0 -> "${minute}分钟前"
            second > 0 -> "${second}秒前"
            else -> "刚刚"
        }
    }
}

