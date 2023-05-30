package com.raweng.dfe.microsdk.featurefeeds.mapper

import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.type.FeedType
import com.raweng.dfe.microsdk.featurefeeds.type.SourceType

class FeaturedFeedsMapper(private val featuredFeeds: FeatureFeedResponse.Entry) {

    fun getFeatureFeeds(): FeaturedFeedModel {
        return FeaturedFeedModel().apply {
            uid = featuredFeeds.uid
            title = featuredFeeds.title
            order = featuredFeeds.order
            feeds = getFeedList()
        }
    }

    //TODO
    private fun getFeedList(): List<FeaturedFeedModel.FeedModel> {
        return featuredFeeds.feedType?.map {
            FeaturedFeedModel.FeedModel(
                feedType = getFeedType(it),
                title = getTitle(it),
                publishedDate = getDate(it),
                fullWidthImageUrl = getFullWidthImageUrl(it),
                halfWidthImageUrl = getHalfWidthImageUrl(it),
                hideDate = getHideDate(it),
                hideTitle = getHideTitle(it),
                hideLabel = getHideLabel(it),
                clickThroughLink = getClickThroughLink(it), //TODO need to handle
                uid = getUid(it),
                nid = getNid(it),
                source = getSource(it),
            )
        } ?: arrayListOf()
    }


    private fun getFeedType(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return when {
            feedType?.article != null -> FeedType.ARTICLE.toString()
            feedType?.video != null -> FeedType.VIDEO.toString()
            feedType?.webUrl != null -> FeedType.WEB_URL.toString()
            feedType?.gallery != null -> FeedType.GALLERY.toString()
            feedType?.nbaFeeds != null -> FeedType.NBA_FEEDS.toString()
            else -> null
        }
    }


    private fun getTitle(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return when {
            feedType?.article != null -> feedType.article?.title
            feedType?.video != null -> feedType.video?.title
            feedType?.webUrl != null -> feedType.webUrl?.title
            feedType?.gallery != null -> feedType.gallery?.title
            feedType?.nbaFeeds != null -> feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.title
            else -> null
        }
    }

    private fun getDate(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return when {
            feedType?.article != null -> feedType.article?.publishedDate
            feedType?.video != null -> feedType.video?.publishedDate
            feedType?.webUrl != null -> feedType.webUrl?.publishedDate
            feedType?.gallery != null -> feedType.gallery?.publishedDate
            feedType?.nbaFeeds != null -> feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.publishedDateString
            else -> null
        }
    }

    private fun getFullWidthImageUrl(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return when {
            feedType?.article != null -> feedType.article?.fullWidthImage?.url
            feedType?.video != null -> feedType.video?.fullWidthImage?.url
            feedType?.webUrl != null -> feedType.webUrl?.fullWidthImage?.url
            feedType?.gallery != null -> feedType.gallery?.fullWidthImage?.url
            feedType?.nbaFeeds != null -> ""
            else -> null
        }
    }


    private fun getHalfWidthImageUrl(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return when {
            feedType?.article != null -> feedType.article?.halfWidthImage?.url
            feedType?.video != null -> feedType.video?.halfWidthImage?.url
            feedType?.webUrl != null -> feedType.webUrl?.halfWidthImage?.url
            feedType?.gallery != null -> feedType.gallery?.halfWidthImage?.url
            feedType?.nbaFeeds != null -> getDFEThumbnail(feedType)
            else -> null
        }
    }

    private fun getDFEThumbnail(feedType: FeatureFeedResponse.Entry.FeedType?): String {
        return feedType?.nbaFeeds?.nbaFeeds?.nbaFeedModel?.media?.get(0)?.thumbnail.orEmpty()
    }

    private fun getHideDate(feedType: FeatureFeedResponse.Entry.FeedType?): Boolean? {
        return when {
            feedType?.article != null -> feedType.article?.hideDate
            feedType?.video != null -> feedType.video?.hideDate
            feedType?.webUrl != null -> feedType.webUrl?.hideDate
            feedType?.gallery != null -> feedType.gallery?.hideDate
            feedType?.nbaFeeds != null -> false
            else -> null
        }
    }

    private fun getHideTitle(feedType: FeatureFeedResponse.Entry.FeedType?): Boolean? {
        return when {
            feedType?.article != null -> feedType.article?.hideTitle
            feedType?.video != null -> feedType.video?.hideTitle
            feedType?.webUrl != null -> feedType.webUrl?.hideTitle
            feedType?.gallery != null -> feedType.gallery?.hideTitle
            feedType?.nbaFeeds != null -> false
            else -> null
        }
    }


    private fun getHideLabel(feedType: FeatureFeedResponse.Entry.FeedType?): Boolean? {
        return when {
            feedType?.article != null -> feedType.article?.hideLabel
            feedType?.video != null -> feedType.video?.hideLabel
            feedType?.webUrl != null -> feedType.webUrl?.hideLabel
            feedType?.gallery != null -> feedType.gallery?.hideDate
            feedType?.nbaFeeds != null -> false
            else -> null
        }
    }


    private fun getClickThroughLink(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return when {
            feedType?.webUrl != null -> feedType.webUrl?.link
            feedType?.video != null -> feedType.video?.videoLink
            else -> null
        }
    }

    private fun getUid(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return when {
            feedType?.article != null -> feedType.article?.metadata?.uid
            feedType?.video != null -> feedType.video?.metadata?.uid
            feedType?.webUrl != null -> feedType.webUrl?.metadata?.uid
            feedType?.gallery != null -> feedType.gallery?.metadata?.uid
            feedType?.nbaFeeds != null -> feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.uid
            else -> null
        }
    }

    private fun getNid(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return feedType?.nbaFeeds?.nbaFeeds?.value
    }

    private fun getSource(feedType: FeatureFeedResponse.Entry.FeedType?): String {
        return if (feedType?.nbaFeeds?.nbaFeeds?.value?.isNotEmpty() == true) {
            SourceType.DFE.toString()
        } else {
            SourceType.CONTENTSTACK.toString()
        }
    }
}