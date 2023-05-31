package com.raweng.dfe.microsdk.featurefeeds.type

enum class FeedType {
    ARTICLE, WEB_URL, VIDEO, GALLERY, NBA_FEEDS;

    override fun toString(): String {
        return when (this) {
            ARTICLE -> ARTICLE.name.uppercase()
            WEB_URL -> WEB_URL.name.uppercase()
            VIDEO -> VIDEO.name.uppercase()
            GALLERY -> GALLERY.name.uppercase()
            NBA_FEEDS -> NBA_FEEDS.name.uppercase()
        }
    }
}