package com.raweng.microsdkapplication.data.model

data class Config(
    var cmsApiKey: String? = null,
    var cmsAccessToken: String? = null,
    var dfeSportsKey: String? = null,
    var cmsApiVersion: String? = null,
    var dfeApiVersion: String? = null,
    var contentType: ContentType? = null,
    var environment: Environment? = null,
)

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
