package com.raweng.dfe.microsdk.featurefeeds.model

import com.raweng.dfe.microsdk.featurefeeds.type.FeedType

data class FeaturedFeedModel(
    var uid: String? = "",
    var title: String? = "",
    var order: Int? = 0,
    var feeds: List<FeedModel>? = null
) {
    data class FeedModel(
        var feedType: String? = FeedType.ARTICLE.toString(),
        var title: String? = null,
        var publishedDate: String? = null,
        var fullWidthImageUrl: String? = null,
        var halfWidthImageUrl: String? = null,
        var hideDate: Boolean? = false,
        var hideTitle: Boolean? = false,
        var hideLabel: Boolean? = false,
        var clickThroughLink: String?,
        var uid: String? = null,
        var nid: String? = null,
        var source: String? = null
    )
}

