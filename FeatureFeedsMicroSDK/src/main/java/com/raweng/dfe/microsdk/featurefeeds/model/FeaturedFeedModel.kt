package com.raweng.dfe.microsdk.featurefeeds.model

data class FeaturedFeedModel(
    var title: String? = "",
    var isoDate:String? = "",
    var updatedAt: String? = "",
    var uid: String? = "",
    var order: Int? = 0,
    var feeds: List<FeedModel>? = null
) {
    data class FeedModel(
        var title: String? = null,
        var date: String? = null,
        var thumbnail: String? = null,
        var feedType: String? = null,
        var feedId: String? = null,
        var feedPosition: Int? = null,
        var label: String? = null,
        var hideTitle: Boolean? = false,
        var hideDate: Boolean? = false,
        var hideLabel: Boolean? = false,
        var dataSource: String? = null,
        var clickthroughLink: String? = null,
        var link: String? = null,
        var content: String? = null,
        var additionalContent: String? = null,
        var galleryImages: List<ImageModel>? = null,
        var spotlightImage: Any? = null,
        var author: AuthorModel? = null,
    )
}

