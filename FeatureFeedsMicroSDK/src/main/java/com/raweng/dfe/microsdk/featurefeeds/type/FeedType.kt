package com.raweng.dfe.microsdk.featurefeeds.type

enum class FeedType {
    ARTICLE, WEB_URL, VIDEO, GALLERY, NBA_FEEDS;

    override fun toString(): String {
        return when (this) {
            ARTICLE -> ARTICLE.name.lowercase()
            WEB_URL -> WEB_URL.name.lowercase()
            VIDEO -> VIDEO.name.lowercase()
            GALLERY -> GALLERY.name.lowercase()
            NBA_FEEDS -> NBA_FEEDS.name.lowercase()
        }
    }
}