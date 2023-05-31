package com.raweng.dfe.microsdk.featurefeeds.type

enum class DateFormat {
    HOURS_AGO, STANDARD_DATE;

    override fun toString(): String {
        return when (this) {
            HOURS_AGO -> HOURS_AGO.name.lowercase()
            STANDARD_DATE -> STANDARD_DATE.name.lowercase()
        }
    }
}