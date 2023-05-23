package com.raweng.dfe.microsdk.featurefeeds.model

data class FeatureFeedResponse(
    val entries: List<Entry>
)

data class Entry(
    val _in_progress: Boolean?,
    val _version: Int?,
    val created_at: String?,
    val created_by: String?,
    val feed_type: List<FeedType>?,
    val locale: String?,
    val order: Int?,
    val publish_details: PublishDetails?,
    val tags: List<Any>?,
    val title: String?,
    val uid: String?,
    val updated_at: String?,
    val updated_by: String?
)

data class FeedType(
    val article: Article?,
    val gallery: Gallery?,
    val nba_feeds: NbaFeeds?,
    val video: Video?,
    val web_url: WebUrl?
)

data class Article(
    val _metadata: Metadata?,
    val additional_content: String?,
    val content: String?,
    val full_width_image: FullWidthImage?,
    val half_width_image: HalfWidthImage?,
    val hide_date: Boolean?,
    val hide_label: Boolean?,
    val hide_title: Boolean?,
    val published_date: String?,
    val spotlight_image: SpotlightImage?,
    val subtitle: String?,
    val title: String?
)

data class Gallery(
    val full_width_image: FullWidthImage?,
    val half_width_image: HalfWidthImage?,
    val hide_date: Boolean?,
    val hide_label: Boolean?,
    val hide_title: Boolean?,
    val images: List<Image>?,
    val published_date: String?,
    val title: String?
)

data class NbaFeeds(
    val full_width_image: FullWidthImage?,
    val half_width_image: HalfWidthImage?,
    val hide_date: Boolean?,
    val hide_label: Boolean?,
    val hide_title: Boolean?,
    val nba_feeds: NbaFeedsX?,
    val type: String?
) {
    data class NbaFeedsX(
        val label: String?,
        val value: String?,
        val mData:Any? = null
    )
}



data class Video(
    val full_width_image: FullWidthImage?,
    val half_width_image: HalfWidthImage?,
    val hide_date: Boolean?,
    val hide_label: Boolean?,
    val hide_title: Boolean?,
    val published_date: String?,
    val title: String?,
    val video_link: String?
)

data class WebUrl(
    val full_width_image: FullWidthImage?,
    val half_width_image: HalfWidthImage?,
    val hide_date: Boolean?,
    val hide_label: Boolean?,
    val hide_title: Boolean?,
    val link: String?,
    val published_date: String?,
    val title: String?
)