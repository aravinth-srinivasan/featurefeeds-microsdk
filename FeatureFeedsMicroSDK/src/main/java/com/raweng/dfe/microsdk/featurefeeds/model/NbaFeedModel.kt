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
    @SerializedName("content")
    var content: String? = null,
    @SerializedName("additional_content")
    var additionalContent: String? = null,
    @SerializedName("published_date")
    var publishedDateString: String? = null,
    @SerializedName("publishedDate")
    var publishedDate: Date? = null,
    @SerializedName("headline")
    var headline: String? = null,
    @SerializedName("subheadline")
    var subHeadline: String? = null,
    @SerializedName("teaser")
    var teaser: String? = null,
    @SerializedName("web_url")
    var webUrl: String? = null,
    @SerializedName("shareable_url")
    var shareableUrl: String? = null,
    @SerializedName("feed_type")
    var feedType: String? = null,
    @SerializedName("category")
    var category: String? = null,
    @SerializedName("media")
    var media: List<FeedMedia?>? = null,
    @SerializedName("video")
    var video: List<FeedVideo?>? = null,
    @SerializedName("taxonomy")
    var taxonomy: FeedTaxanomy? = null,
    @SerializedName("sponser_ads")
    var sponserAds: List<String?>? = null,
    @SerializedName("custom_fields")
    var customFields: String? = null,
    @SerializedName("template_fields")
    var templateFields: String? = null,
    @SerializedName("author")
    var author: Author? = null,

    ) {

    data class FeedMedia(
        @SerializedName("thumbnail")
        var thumbnail: String? = null,
        @SerializedName("large")
        var large: String? = null,
        @SerializedName("alt_text")
        var altText: String? = null,
        @SerializedName("tile")
        var tile: String? = null,
        @SerializedName("mobile")
        var mobile: String? = null,
        @SerializedName("caption")
        var caption: String? = null,
        @SerializedName("source")
        var source: String? = null,
        @SerializedName("type")
        var type: String? = null,
        @SerializedName("portrait")
        var portrait: String? = null,
        @SerializedName("credit")
        var credit: String? = null,
        @SerializedName("landscape")
        var landscape: String? = null,
        @SerializedName("raw")
        var raw: MediaRaw? = null,
    ) {
        data class MediaRaw(
            @SerializedName("size")
            var size: String? = null,
            @SerializedName("focal_point")
            var focalPoint: String? = null,
            @SerializedName("url")
            var url: String? = null,
        )
    }

    data class FeedVideo(
        @SerializedName("video_type")
        var videoType: String? = null,
        @SerializedName("url")
        var url: String? = null,
        @SerializedName("bitrate")
        var bitrate: String? = null,
    )

    data class FeedTaxanomy(
        @SerializedName("coaches")
        var coaches: List<String?>? = null,
        @SerializedName("teams")
        var teams: List<String?>? = null,
        @SerializedName("players")
        var players: List<String?>? = null,
        @SerializedName("freeform")
        var freeform: List<String?>? = null,
        @SerializedName("games")
        var games: List<String?>? = null,
        @SerializedName("section")
        var section: List<String?>? = null,
        @SerializedName("channels")
        var channels: List<String?>? = null,
        @SerializedName("writer")
        var writer: FeedWriter? = null,
    ) {
        data class FeedWriter(
            @SerializedName("email")
            var email: String? = null,
            @SerializedName("value")
            var value: String? = null,
            @SerializedName("id")
            var id: String? = null,
            @SerializedName("title")
            var title: String? = null,
        )
    }

    data class Author(
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("slug")
        var slug: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("first_name")
        var firstName: String? = null,
        @SerializedName("last_name")
        var lastName: String? = null,
        @SerializedName("organization")
        var organization: String? = null,
        @SerializedName("is_nba_staff")
        var isNbaStaff: Boolean? = null,
        @SerializedName("author_photo")
        var authorPhoto: String? = null,
        @SerializedName("social_handle")
        var socialHandle: String? = null,
        @SerializedName("social_link")
        var socialLink: String? = null,
        @SerializedName("wpseo_title")
        var wpseoTitle: String? = null,
        @SerializedName("wpseo_metadesc")
        var wpseoMetadesc: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("user_url")
        var userUrl: String? = null,
        @SerializedName("featured_author")
        var featuredAuthor: Boolean? = null,
    )

}
