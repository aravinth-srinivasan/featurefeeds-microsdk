package com.raweng.dfe.microsdk.featurefeeds.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Utils {

    private const val CMS_DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    fun parseDate(dateString: String): Date? {
        val dateFormat = SimpleDateFormat(CMS_DEFAULT_DATE_FORMAT, Locale.getDefault())
        return dateFormat.parse(dateString)
    }

    fun parseDateFormat(dateString: String?, outputFormat: String): String? {
        val inputDateFormat = SimpleDateFormat(CMS_DEFAULT_DATE_FORMAT, Locale.getDefault())
        val outputDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
        return dateString?.let {dateS ->
            val parsedDate = inputDateFormat.parse(dateS)
            parsedDate?.let { outputDateFormat.format(it) }
        }
    }


    fun convertTimestampToDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    fun timeAgoSinceAccessibility(date: Date, dateFormat: String): String {
        val calendar = Calendar.getInstance()
        val now = Date()
        calendar.time = date
        val timeDifference = now.time - date.time

        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference)
        val hours = TimeUnit.MILLISECONDS.toHours(timeDifference)
        val days = TimeUnit.MILLISECONDS.toDays(timeDifference)
        val weeks = days / 7
        val months = days / 30
        val years = days / 365


        return when {
            years >= 1 -> formatDateToString(date, dateFormat)
            months >= 1 -> formatDateToString(date, dateFormat)
            weeks >= 1 -> formatDateToString(date, dateFormat)
            days >= 1 -> {
                if (days == 1L) {
                    "Yesterday".uppercase()
                } else {
                    SimpleDateFormat(dateFormat, Locale.getDefault()).format(date)
                }
            }
            hours >= 1 -> {
                if (hours == 1L) {
                    "$hours hour ago".uppercase()
                } else {
                    "$hours hours ago".uppercase()
                }
            }
            minutes >= 1 -> {
                if (minutes == 1L) {
                    "Moment ago".uppercase()
                } else {
                    "$minutes minutes ago".uppercase()
                }
            }
            else -> "1 minute ago".uppercase()
        }
    }

    fun formatDateToString(date: Date, format: String): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }
}