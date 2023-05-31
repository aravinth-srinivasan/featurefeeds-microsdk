package com.raweng.dfe.microsdk.featurefeeds.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class NbaFeedModel(

    @SerializedName("uid")
    var uid: String? = null,
    @SerializedName("nid")
    var nid: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("published_date")
    var publishedDateString: String? = null,
    @SerializedName("publishedDate")
    var publishedDate: Date? = null,
    @SerializedName("feed_type")
    var feedType: String? = null,
    @SerializedName("category")
    var category: String? = null,
    @SerializedName("media")
    var media: List<FeedMedia?>? = null,
    var position: Int = -1,
) {
    data class FeedMedia(
        @SerializedName("thumbnail")
        var thumbnail: String? = null,
    )
}
