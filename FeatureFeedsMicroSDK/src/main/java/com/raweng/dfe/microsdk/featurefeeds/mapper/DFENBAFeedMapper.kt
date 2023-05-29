package com.raweng.dfe.microsdk.featurefeeds.mapper

import com.raweng.dfe.microsdk.featurefeeds.model.NbaFeedModel
import com.raweng.dfe.models.feed.DFEFeedModel

class DFENBAFeedMapper(private val dfeFeedModel: DFEFeedModel) {

    fun getNbaFeedModel(): NbaFeedModel {
        return NbaFeedModel().apply {
            uid = dfeFeedModel.uid
            title = dfeFeedModel.title
            media = getMedia()
            publishedDateString = dfeFeedModel.publishedDate
            feedType = dfeFeedModel.feedType
        }
    }

    private fun getMedia(): List<NbaFeedModel.FeedMedia> {
        return dfeFeedModel.media.map {
            NbaFeedModel.FeedMedia().apply {
                thumbnail = it.thumbnail
            }
        }
    }
}