package com.raweng.microsdkapplication.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class FeedModel(

    // DFEFeedModel
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

    // FeatureFeedModel.Entry

    @SerializedName("created_at")
    var createdAt: String? = null,
    @SerializedName("created_by")
    var createdBy: String? = null,
    /*@SerializedName("feed_type")
    var feedType: List<FeedType?>? = null,*/
    @SerializedName("_in_progress")
    var inProgress: Boolean? = null,
    @SerializedName("locale")
    var locale: String? = null,
    @SerializedName("order")
    var order: Int? = null,
    @SerializedName("tags")
    var tags: List<Any?>? = null,
    /*@SerializedName("title")
    var title: String? = null,*/
    /*@SerializedName("uid")
    var uid: String? = null,*/
    @SerializedName("updated_at")
    var updatedAt: String? = null,
    @SerializedName("updated_by")
    var updatedBy: String? = null,
    @SerializedName("_version")
    var version: Int? = null

) : Serializable {

    // DFEFeedModel

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
    ) : Serializable {
        data class MediaRaw(
            @SerializedName("size")
            var size: String? = null,
            @SerializedName("focal_point")
            var focalPoint: String? = null,
            @SerializedName("url")
            var url: String? = null,
        ) : Serializable
    }

    data class FeedVideo(
        @SerializedName("video_type")
        var videoType: String? = null,
        @SerializedName("url")
        var url: String? = null,
        @SerializedName("bitrate")
        var bitrate: String? = null,
    ) : Serializable

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
    ) : Serializable {
        data class FeedWriter(
            @SerializedName("email")
            var email: String? = null,
            @SerializedName("value")
            var value: String? = null,
            @SerializedName("id")
            var id: String? = null,
            @SerializedName("title")
            var title: String? = null,
        ) : Serializable
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
    ) : Serializable

    // FeatureFeedModel.Entry

    data class Image(
        @SerializedName("ACL")
        var aCL: Any? = null,
        @SerializedName("content_type")
        var contentType: String? = null,
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("created_by")
        var createdBy: String? = null,
        @SerializedName("file_size")
        var fileSize: String? = null,
        @SerializedName("filename")
        var filename: String? = null,
        @SerializedName("is_dir")
        var isDir: Boolean? = null,
        @SerializedName("parent_uid")
        var parentUid: Any? = null,
        @SerializedName("tags")
        var tags: List<Any?>? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("uid")
        var uid: String? = null,
        @SerializedName("updated_at")
        var updatedAt: String? = null,
        @SerializedName("updated_by")
        var updatedBy: String? = null,
        @SerializedName("url")
        var url: String? = null,
        @SerializedName("_version")
        var version: Int? = null
    ) : Serializable

    data class Metadata(
        @SerializedName("uid")
        var uid: String? = null
    ) : Serializable

    data class FeedType(
        @SerializedName("article")
        var article: Article? = null,
        @SerializedName("gallery")
        var gallery: Gallery? = null,
        @SerializedName("video")
        var video: Video? = null,
        @SerializedName("web_url")
        var webUrl: WebUrl? = null
    ) : Serializable {

        data class Article(
            @SerializedName("additional_content")
            var additionalContent: String? = null,
            @SerializedName("content")
            var content: String? = null,
            @SerializedName("full_width_image")
            var fullWidthImage: Image? = null,
            @SerializedName("half_width_image")
            var halfWidthImage: Image? = null,
            @SerializedName("hide_date")
            var hideDate: Boolean? = null,
            @SerializedName("hide_title")
            var hideTitle: Boolean? = null,
            @SerializedName("hide_label")
            var hideLabel: Boolean? = null,
            @SerializedName("_metadata")
            var metadata: Metadata? = null,
            @SerializedName("published_date")
            var publishedDate: String? = null,
            @SerializedName("spotlight_image")
            var spotlightImage: Image? = null,
            @SerializedName("subtitle")
            var subtitle: String? = null,
            @SerializedName("title")
            var title: String? = null
        ) : Serializable

        data class Gallery(
            @SerializedName("full_width_image")
            var fullWidthImage: Image? = null,
            @SerializedName("half_width_image")
            var halfWidthImage: Image? = null,
            @SerializedName("hide_date")
            var hideDate: Boolean? = null,
            @SerializedName("hide_title")
            var hideTitle: Boolean? = null,
            @SerializedName("hide_label")
            var hideLabel: Boolean? = null,
            @SerializedName("images")
            var images: List<CaptionImage?>? = null,
            @SerializedName("_metadata")
            var metadata: Metadata? = null,
            @SerializedName("published_date")
            var publishedDate: String? = null,
            @SerializedName("title")
            var title: String? = null
        ) : Serializable {

            data class CaptionImage(
                @SerializedName("caption")
                var caption: String? = null,
                @SerializedName("image")
                var image: Image? = null,
                @SerializedName("_metadata")
                var metadata: Metadata? = null
            ) : Serializable

        }

        data class Video(
            @SerializedName("full_width_image")
            var fullWidthImage: Image? = null,
            @SerializedName("half_width_image")
            var halfWidthImage: Image? = null,
            @SerializedName("hide_date")
            var hideDate: Boolean? = null,
            @SerializedName("hide_title")
            var hideTitle: Boolean? = null,
            @SerializedName("hide_label")
            var hideLabel: Boolean? = null,
            @SerializedName("_metadata")
            var metadata: Metadata? = null,
            @SerializedName("published_date")
            var publishedDate: String? = null,
            @SerializedName("title")
            var title: String? = null,
            @SerializedName("video_link")
            var videoLink: String? = null
        ) : Serializable

        data class WebUrl(
            @SerializedName("full_width_image")
            var fullWidthImage: Image? = null,
            @SerializedName("half_width_image")
            var halfWidthImage: Image? = null,
            @SerializedName("hide_date")
            var hideDate: Boolean? = null,
            @SerializedName("hide_title")
            var hideTitle: Boolean? = null,
            @SerializedName("hide_label")
            var hideLabel: Boolean? = null,
            @SerializedName("link")
            var link: String? = null,
            @SerializedName("_metadata")
            var metadata: Metadata? = null,
            @SerializedName("published_date")
            var publishedDate: String? = null,
            @SerializedName("title")
            var title: String? = null
        ) : Serializable
    }
}
