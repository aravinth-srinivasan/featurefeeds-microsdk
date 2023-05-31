package com.raweng.dfe.microsdk.featurefeeds.type

enum class SourceType {
    CONTENTSTACK, DFE;

    override fun toString(): String {
        return when (this) {
            CONTENTSTACK -> CONTENTSTACK.name.uppercase()
            DFE -> DFE.name.uppercase()
        }
    }
}