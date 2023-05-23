package com.raweng.microsdkapplication.data



enum class Environment {
    DEV,
    PRODUCTION,
    PREVIEW
}

enum class ContentType {
    FEATURED_FEEDS,
    CONTENTHUB_FEEDS,
    FEEDS_ON_ARENAHUB;

    override fun toString(): String {
        return super.toString().lowercase()
    }
}
