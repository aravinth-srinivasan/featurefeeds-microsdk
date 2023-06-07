package com.raweng.dfe.microsdk.featurefeeds.mapper

import com.raweng.dfe.microsdk.featurefeeds.model.AuthorModel
import com.raweng.dfe.microsdk.featurefeeds.model.NbaFeedModel
import com.raweng.dfe.models.feed.DFEFeedModel

class DFENBAFeedMapper(private val feedPosition: Int, private val dfeFeedModel: DFEFeedModel) {

    fun getNbaFeedModel(): NbaFeedModel {
        return NbaFeedModel().apply {
            uid = dfeFeedModel.uid
            nid = dfeFeedModel.newsid
            title = dfeFeedModel.title
            media = getMedia()
            publishedDateString = dfeFeedModel.publishedDate
            feedType = dfeFeedModel.feedType
            position = feedPosition
            content = dfeFeedModel.content
            additionalContent = dfeFeedModel.additionalContent
            video = getVideo()
            webUrl = dfeFeedModel.webUrl
            author = dfeFeedModel.`realmGet$author`().let {
                AuthorModel().apply {
                    authorName = it.name
                    authorImage = it.authorPhoto
                    organization = it.organization
                    isNbaStaff = it.isIsNbaStaff
                    description = it.description
                    isFeaturedAuthor = it.isFeaturedAuthor
                }
            }
        }
    }

    private fun getMedia(): List<NbaFeedModel.FeedMedia> {
        return dfeFeedModel.media.map {
            NbaFeedModel.FeedMedia().apply {
                thumbnail = it.thumbnail
                source = it.source
                caption = it.caption
                type = it.type
            }
        }
    }

    private fun getVideo(): List<NbaFeedModel.Video> {
        return dfeFeedModel.video.map {
            NbaFeedModel.Video().apply {
                url = it.url
            }
        }
    }
}