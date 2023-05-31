package com.raweng.dfe.microsdk.featurefeeds.mapper

import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.type.DateFormat
import com.raweng.dfe.microsdk.featurefeeds.type.FeedType
import com.raweng.dfe.microsdk.featurefeeds.type.SourceType
import com.raweng.dfe.microsdk.featurefeeds.utils.Utils

class FeaturedFeedsMapper(
    private val appScheme: String?,
    private val dateFormatType: DateFormat?,
    private val dateFormat: String,
    private val featuredFeeds: FeatureFeedResponse.Entry
) {

    fun getFeatureFeeds(): FeaturedFeedModel {
        return FeaturedFeedModel().apply {
            title = featuredFeeds.title
            updatedAt = featuredFeeds.updatedAt
            uid = featuredFeeds.uid
            order = featuredFeeds.order
            feeds = getFeedList()
        }
    }

    private fun getFeedList(): List<FeaturedFeedModel.FeedModel> {
        return featuredFeeds.feedType?.map {
            FeaturedFeedModel.FeedModel(
                title = getTitle(it),
                date = getDate(it),
                thumbnail = getThumbnail(featuredFeeds.feedType?.size, it),
                feedType = getFeedType(it),
                feedId = getFeedId(it),
                feedPosition = getFeedPosition(it),
                hideDate = getHideDate(it),
                hideTitle = getHideTitle(it),
                hideLabel = getHideLabel(it),
                clickthroughLink = "",
                dataSource = getSource(it),
            ).apply {
                clickthroughLink = getGenerateClickThroughLink(this)
            }
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
        val publishedDate: String? = when {
            feedType?.article != null -> feedType.article?.publishedDate
            feedType?.video != null -> feedType.video?.publishedDate
            feedType?.webUrl != null -> feedType.webUrl?.publishedDate
            feedType?.gallery != null -> feedType.gallery?.publishedDate
            feedType?.nbaFeeds != null -> feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.publishedDateString
            else -> null
        }

        if (!publishedDate.isNullOrEmpty()) {
            val date = if (feedType?.nbaFeeds is FeatureFeedResponse.Entry.FeedType.NbaFeeds) {
                Utils.convertTimestampToDate(publishedDate.toLong())
            } else {
                Utils.parseDate(publishedDate)
            }
            if (dateFormatType == DateFormat.HOURS_AGO) {
                return date?.let { Utils.timeAgoSinceAccessibility(it,dateFormat) }
            } else {
                return date?.let { Utils.formatDateToString(it,dateFormat) }
            }
        }
        return null
    }

    private fun getThumbnail(
        totalSize: Int?,
        feedType: FeatureFeedResponse.Entry.FeedType?
    ): String? {
        val totalSizeData = totalSize ?: 0
        return when {
            feedType?.article != null -> if (totalSizeData >= 1) (feedType.article?.halfWidthImage?.url) else (feedType.article?.fullWidthImage?.url)
            feedType?.video != null -> if (totalSizeData >= 1) (feedType.video?.halfWidthImage?.url) else (feedType.video?.fullWidthImage?.url)
            feedType?.webUrl != null -> if (totalSizeData >= 1) (feedType.webUrl?.halfWidthImage?.url) else (feedType.webUrl?.fullWidthImage?.url)
            feedType?.gallery != null -> if (totalSizeData >= 1) (feedType.gallery?.halfWidthImage?.url) else (feedType.gallery?.fullWidthImage?.url)
            feedType?.nbaFeeds != null -> getDFEThumbnail(totalSizeData, feedType)
            else -> null
        }
    }


    private fun getDFEThumbnail(
        totalSize: Int,
        feedType: FeatureFeedResponse.Entry.FeedType?
    ): String {
        return if (totalSize >= 1) {
            feedType?.nbaFeeds?.nbaFeeds?.halfWidthImage?.url
                ?: feedType?.nbaFeeds?.nbaFeeds?.fullWidthImage?.url
                ?: feedType?.nbaFeeds?.nbaFeeds?.nbaFeedModel?.media?.getOrNull(0)?.thumbnail
                ?: ""
        } else {
            feedType?.nbaFeeds?.nbaFeeds?.fullWidthImage?.url
                ?: feedType?.nbaFeeds?.nbaFeeds?.halfWidthImage?.url
                ?: feedType?.nbaFeeds?.nbaFeeds?.nbaFeedModel?.media?.getOrNull(0)?.thumbnail
                ?: ""
        }
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


    private fun getGenerateClickThroughLink(feedType: FeaturedFeedModel.FeedModel): String {
        return "$appScheme://screen/feed_detail/${feedType.feedType?.lowercase() ?: ""}?position=${feedType.feedPosition ?: 0}&source=${feedType.dataSource?.lowercase() ?: ""}&feed_id=${feedType.feedId ?: ""}"
    }

    private fun getFeedId(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return when {
            feedType?.article != null -> feedType.article?.metadata?.uid
            feedType?.video != null -> feedType.video?.metadata?.uid
            feedType?.webUrl != null -> feedType.webUrl?.metadata?.uid
            feedType?.gallery != null -> feedType.gallery?.metadata?.uid
            feedType?.nbaFeeds != null -> feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.nid
            else -> null
        }
    }


    private fun getFeedPosition(feedType: FeatureFeedResponse.Entry.FeedType?): Int? {
        return when {
            feedType?.article != null -> feedType.article?.position
            feedType?.video != null -> feedType.video?.position
            feedType?.gallery != null -> feedType.gallery?.position
            feedType?.webUrl != null -> feedType.webUrl?.position
            feedType?.nbaFeeds != null -> feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.position
            else -> null
        }
    }

    private fun getSource(feedType: FeatureFeedResponse.Entry.FeedType?): String {
        return if (feedType?.nbaFeeds?.nbaFeeds?.value?.isNotEmpty() == true) {
            SourceType.DFE.toString()
        } else {
            SourceType.CONTENTSTACK.toString()
        }
    }
}