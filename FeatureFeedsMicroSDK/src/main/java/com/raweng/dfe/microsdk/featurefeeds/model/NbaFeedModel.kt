package com.raweng.dfe.microsdk.featurefeeds.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class NbaFeedModel(

    var uid: String? = null,
    var nid: String? = null,
    var title: String? = null,
    @SerializedName("published_date")
    var publishedDateString: String? = null,
    @SerializedName("publishedDate")
    var publishedDate: Date? = null,
    @SerializedName("feed_type")
    var feedType: String? = null,
    var category: String? = null,
    var media: List<FeedMedia?>? = null,
    var content: String? = null,
    @SerializedName("additional_content")
    var additionalContent: String? = null,
    @SerializedName("web_url")
    var webUrl: String? = null,
    var video: List<Video?>? = null,
    var position: Int = -1,
    var author: AuthorModel? = null
) {
    data class FeedMedia(
        var thumbnail: String? = null,
        var source: String? = null,
        var caption: String? = null,
        var type: String? = null,
        var title: String? = null
    )

    data class Video(var url: String? = null)

}

data class AuthorModel(
    var authorName: String? = null,
    var organization: String? = null,
    var authorImage: String? = null,
    var isNbaStaff: Boolean? = null,
    var description: String? = null,
    var isFeaturedAuthor: Boolean? = null
)
