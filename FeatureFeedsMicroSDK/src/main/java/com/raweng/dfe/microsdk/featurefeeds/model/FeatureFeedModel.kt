package com.raweng.dfe.microsdk.featurefeeds.model


import com.google.gson.annotations.SerializedName

data class FeatureFeedModel(
    @SerializedName("entries")
    var entries: List<Entry?>? = null
) {

    class ACL

    data class PublishDetails(
        @SerializedName("environment")
        var environment: String? = null,
        @SerializedName("locale")
        var locale: String? = null,
        @SerializedName("time")
        var time: String? = null,
        @SerializedName("user")
        var user: String? = null
    )

    data class Metadata(
        @SerializedName("uid")
        var uid: String? = null
    )

    data class Image(
        @SerializedName("ACL")
        var aCL: ACL? = null,
        @SerializedName("content_type")
        var contentType: String? = null,
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("created_by")
        var createdBy: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("file_size")
        var fileSize: String? = null,
        @SerializedName("filename")
        var filename: String? = null,
        @SerializedName("is_dir")
        var isDir: Boolean? = null,
        @SerializedName("parent_uid")
        var parentUid: Any? = null,
        @SerializedName("publish_details")
        var publishDetails: PublishDetails? = null,
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
    )

    data class Entry(
        @SerializedName("ACL")
        var aCL: ACL? = null,
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("created_by")
        var createdBy: String? = null,
        @SerializedName("feed_type")
        var feedType: List<FeedType?>? = null,
        @SerializedName("_in_progress")
        var inProgress: Boolean? = null,
        @SerializedName("locale")
        var locale: String? = null,
        @SerializedName("order")
        var order: Int? = null,
        @SerializedName("publish_details")
        var publishDetails: PublishDetails? = null,
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
        @SerializedName("_version")
        var version: Int? = null
    ) {

        data class FeedType(
            @SerializedName("gallery")
            var gallery: Gallery? = null,
            @SerializedName("nba_feeds")
            var nbaFeeds: NbaFeeds? = null,
            @SerializedName("video")
            var video: Video? = null,
            @SerializedName("web_url")
            var webUrl: WebUrl? = null
        ) {
            data class Gallery(
                @SerializedName("full_width_image")
                var fullWidthImage: Any? = null,
                @SerializedName("half_width_image")
                var halfWidthImage: Image? = null,
                @SerializedName("hide_date")
                var hideDate: Boolean? = null,
                @SerializedName("hide_label")
                var hideLabel: Boolean? = null,
                @SerializedName("hide_title")
                var hideTitle: Boolean? = null,
                @SerializedName("images")
                var images: List<GalleryImage?>? = null,
                @SerializedName("_metadata")
                var metadata: Metadata? = null,
                @SerializedName("published_date")
                var publishedDate: String? = null,
                @SerializedName("title")
                var title: String? = null
            ) {

                data class GalleryImage(
                    @SerializedName("caption")
                    var caption: String? = null,
                    @SerializedName("image")
                    var image: Image? = null,
                    @SerializedName("_metadata")
                    var metadata: Metadata? = null
                )

            }

            data class NbaFeeds(
                @SerializedName("full_width_image")
                var fullWidthImage: Image? = null,
                @SerializedName("half_width_image")
                var halfWidthImage: Any? = null,
                @SerializedName("hide_date")
                var hideDate: Boolean? = null,
                @SerializedName("hide_label")
                var hideLabel: Boolean? = null,
                @SerializedName("hide_title")
                var hideTitle: Boolean? = null,
                @SerializedName("_metadata")
                var metadata: Metadata? = null,
                @SerializedName("nba_feeds")
                var nbaFeeds: NbaFeeds? = null,
                @SerializedName("type")
                var type: String? = null
            ) {

                data class NbaFeeds(
                    @SerializedName("label")
                    var label: String? = null,
                    @SerializedName("value")
                    var value: String? = null
                )
            }

            data class Video(
                @SerializedName("full_width_image")
                var fullWidthImage: Any? = null,
                @SerializedName("half_width_image")
                var halfWidthImage: Image? = null,
                @SerializedName("hide_date")
                var hideDate: Boolean? = null,
                @SerializedName("hide_label")
                var hideLabel: Boolean? = null,
                @SerializedName("hide_title")
                var hideTitle: Boolean? = null,
                @SerializedName("_metadata")
                var metadata: Metadata? = null,
                @SerializedName("published_date")
                var publishedDate: String? = null,
                @SerializedName("title")
                var title: String? = null,
                @SerializedName("video_link")
                var videoLink: String? = null
            )

            data class WebUrl(
                @SerializedName("full_width_image")
                var fullWidthImage: Image? = null,
                @SerializedName("half_width_image")
                var halfWidthImage: Any? = null,
                @SerializedName("hide_date")
                var hideDate: Boolean? = null,
                @SerializedName("hide_label")
                var hideLabel: Boolean? = null,
                @SerializedName("hide_title")
                var hideTitle: Boolean? = null,
                @SerializedName("link")
                var link: String? = null,
                @SerializedName("_metadata")
                var metadata: Metadata? = null,
                @SerializedName("published_date")
                var publishedDate: String? = null,
                @SerializedName("title")
                var title: String? = null
            )
        }


    }
}