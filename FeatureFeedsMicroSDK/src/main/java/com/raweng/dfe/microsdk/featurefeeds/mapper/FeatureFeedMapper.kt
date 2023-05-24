package com.raweng.dfe.microsdk.featurefeeds.mapper

import com.raweng.dfe.microsdk.featurefeeds.model.NbaFeedModel
import com.raweng.dfe.models.feed.DFEFeedModel
import com.raweng.dfe.models.feed.FeedMedia

class FeatureFeedMapper(private val dfeFeedModel: DFEFeedModel) {

    fun getNbaFeedModel(): NbaFeedModel {
        return NbaFeedModel().apply {
            title = dfeFeedModel.title
            media = getMedia()
            publishedDateString = dfeFeedModel.publishedDate
            feedType = dfeFeedModel.feedType
            content = dfeFeedModel.content
            additionalContent = dfeFeedModel.additionalContent
        }
    }

    private fun getMedia(): List<NbaFeedModel.FeedMedia> {
        return dfeFeedModel.media.map {
            val feedMedia = NbaFeedModel.FeedMedia().apply {
                thumbnail = it.thumbnail
                large = it.large
                altText = it.alt_text
                tile = it.tile
                mobile = it.mobile
                caption = it.caption
                source = it.source
                type = it.type
                portrait = it.portrait
                credit = it.credit
                landscape = it.landscape
                raw = getRaw(it)
            }
            feedMedia
        }
    }

    private fun getRaw(it: FeedMedia): NbaFeedModel.FeedMedia.MediaRaw {
        return NbaFeedModel.FeedMedia.MediaRaw().apply {
            size = it.raw.size
            focalPoint = it.raw.focalPoint
            url = it.raw.url
        }
    }
}