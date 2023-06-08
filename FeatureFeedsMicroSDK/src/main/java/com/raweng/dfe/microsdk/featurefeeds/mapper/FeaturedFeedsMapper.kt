package com.raweng.dfe.microsdk.featurefeeds.mapper

import com.raweng.dfe.microsdk.featurefeeds.FeatureFeedsMicroSDK
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.model.GalleryModel
import com.raweng.dfe.microsdk.featurefeeds.type.DateFormat
import com.raweng.dfe.microsdk.featurefeeds.type.FeedType
import com.raweng.dfe.microsdk.featurefeeds.type.SourceType
import com.raweng.dfe.microsdk.featurefeeds.utils.Utils

class FeaturedFeedsMapper(private val featuredFeeds: FeatureFeedResponse.Entry) {

    private companion object {
        private const val CONTENT_STACK_URL = "https://images.contentstack.io"
    }

    fun getFeatureFeeds(): FeaturedFeedModel {
        return FeaturedFeedModel().apply {
            title = featuredFeeds.title
            isoDate = featuredFeeds.updatedAt
            updatedAt = getUpdatedAtDate()
            uid = featuredFeeds.uid
            order = featuredFeeds.order
            feeds = getFeedList(this)
        }
    }

    private fun getUpdatedAtDate(): String? {
        return try {
            val date = Utils.parseDate(featuredFeeds.updatedAt.orEmpty())
            date?.let {
                val dataFormat = FeatureFeedsMicroSDK.getCSDateFormat()
                if (FeatureFeedsMicroSDK.getCSDateFormatType() == DateFormat.HOURS_AGO) {
                    Utils.timeAgoSinceAccessibility(it, dataFormat)
                } else {
                    Utils.formatDateToString(it, dataFormat)
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getFeedList(mFeatureFeedModel: FeaturedFeedModel): List<FeaturedFeedModel.FeedModel> {
        return featuredFeeds.feedType?.map {
            FeaturedFeedModel.FeedModel(
                title = getTitle(it),
                date = getDate(it),
                thumbnail = getThumbnail(featuredFeeds.feedType?.size, it),
                feedType = getFeedType(it)?.lowercase(),
                label = getFeedType(it),
                feedId = getFeedId(it),
                feedPosition = getFeedPosition(it),
                hideDate = getHideDate(it),
                hideTitle = getHideTitle(it),
                hideLabel = getHideLabel(it),
                clickthroughLink = "",
                dataSource = getSource(it),
                content = getContent(it),
                additionalContent = getAdditionalContent(it),
                galleryImages = getGalleryImages(it),
                link = getLink(it),
                spotlightImage = getSpotlightImage(it),
                author = it?.nbaFeeds?.nbaFeeds?.nbaFeedModel?.author

            ).apply {
                clickthroughLink = getGenerateClickThroughLink(mFeatureFeedModel, this)
            }
        } ?: listOf()
    }

    private fun getSpotlightImage(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        val url = when {
            feedType?.article != null -> feedType.article?.spotlightImage?.url.orEmpty()
            feedType?.video != null -> ""
            feedType?.webUrl != null -> ""
            feedType?.gallery != null -> ""
            feedType?.nbaFeeds != null -> feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.media?.getOrNull(0)?.portrait.orEmpty()
            else -> null
        }
        return generateCMSImageUrl(url)
    }

    private fun getLink(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return if (!feedType?.article?.link.isNullOrEmpty()) {
            feedType?.article?.link.orEmpty()
        } else if (!feedType?.gallery?.link.isNullOrEmpty()) {
            feedType?.gallery?.link.orEmpty()
        } else if (!feedType?.webUrl?.link.isNullOrEmpty()) {
            feedType?.webUrl?.link.orEmpty()
        } else {
            getVideoLink(feedType)
        }
    }

    private fun getVideoLink(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return if (!feedType?.video?.videoLink.isNullOrEmpty()) {
            feedType?.video?.videoLink.orEmpty()
        } else if (!feedType?.nbaFeeds?.nbaFeeds?.nbaFeedModel?.video.isNullOrEmpty()) {
            feedType?.nbaFeeds?.nbaFeeds?.nbaFeedModel?.video?.getOrNull(0)?.url
        } else {
            ""
        }
    }

    private fun getGalleryImages(feedType: FeatureFeedResponse.Entry.FeedType?): List<GalleryModel>? {
        return if (feedType?.gallery != null) {
            feedType.gallery?.images?.map {
                GalleryModel(
                    url = generateCMSImageUrl(it?.image?.url.orEmpty()),
                    imageTitle = it?.image?.title.orEmpty(),
                    imageType = it?.image?.contentType.orEmpty(),
                    caption = it?.caption.orEmpty()
                )
            }
        } else if (feedType?.nbaFeeds?.nbaFeeds?.nbaFeedModel?.media != null) {
            feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.media?.map {
                GalleryModel(
                    url = generateCMSImageUrl(it?.source.orEmpty()),
                    caption = it?.caption.orEmpty(),
                    imageTitle = it?.title.orEmpty(),
                    imageType = it?.type.orEmpty()
                )
            }
        } else {
            listOf()
        }
    }

    private fun getContent(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return when {
            feedType?.article != null -> feedType.article?.content.orEmpty()
            feedType?.video != null -> feedType.video?.content.orEmpty()
            feedType?.webUrl != null -> feedType.webUrl?.content.orEmpty()
            feedType?.gallery != null -> feedType.gallery?.content.orEmpty()
            feedType?.nbaFeeds != null -> feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.content.orEmpty()
            else -> null
        }
    }

    private fun getAdditionalContent(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return when {
            feedType?.article != null -> feedType.article?.additionalContent.orEmpty()
            feedType?.video != null -> feedType.video?.additionalContent.orEmpty()
            feedType?.webUrl != null -> feedType.webUrl?.additionalContent.orEmpty()
            feedType?.gallery != null -> feedType.gallery?.additionalContent.orEmpty()
            feedType?.nbaFeeds != null -> feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.additionalContent.orEmpty()
            else -> null
        }
    }


    private fun getFeedType(feedType: FeatureFeedResponse.Entry.FeedType?): String? {
        return when {
            feedType?.article != null -> FeedType.ARTICLE.toString()
            feedType?.video != null -> FeedType.VIDEO.toString()
            feedType?.webUrl != null -> FeedType.WEB_URL.toString()
            feedType?.gallery != null -> FeedType.GALLERY.toString()
            feedType?.nbaFeeds != null -> feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.feedType?.uppercase() ?: ""
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
            val dataFormat = FeatureFeedsMicroSDK.getCSDateFormat()
            return if (FeatureFeedsMicroSDK.getCSDateFormatType() == DateFormat.HOURS_AGO) {
                date?.let { Utils.timeAgoSinceAccessibility(it, dataFormat) }
            } else {
                date?.let { Utils.formatDateToString(it, dataFormat) }
            }
        }
        return null
    }

    private fun getThumbnail(
        totalSize: Int?,
        feedType: FeatureFeedResponse.Entry.FeedType?
    ): String? {
        val totalSizeData = totalSize ?: 0
        val url = when {
            feedType?.article != null -> if (totalSizeData > 1) (feedType.article?.halfWidthImage?.url) else (feedType.article?.fullWidthImage?.url)
            feedType?.video != null -> if (totalSizeData > 1) (feedType.video?.halfWidthImage?.url) else (feedType.video?.fullWidthImage?.url)
            feedType?.webUrl != null -> if (totalSizeData > 1) (feedType.webUrl?.halfWidthImage?.url) else (feedType.webUrl?.fullWidthImage?.url)
            feedType?.gallery != null -> if (totalSizeData > 1) (feedType.gallery?.halfWidthImage?.url) else (feedType.gallery?.fullWidthImage?.url)
            feedType?.nbaFeeds != null -> getDFEThumbnail(totalSizeData, feedType)
            else -> null
        }
        return generateCMSImageUrl(url)
    }


    private fun getDFEThumbnail(
        totalSize: Int,
        feedType: FeatureFeedResponse.Entry.FeedType?
    ): String {
        return if (totalSize > 1) {
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
            feedType?.gallery != null -> feedType.gallery?.hideLabel
            feedType?.nbaFeeds != null -> false
            else -> null
        }
    }


    private fun getGenerateClickThroughLink(
        mFeatureFeedModel: FeaturedFeedModel,
        feedType: FeaturedFeedModel.FeedModel
    ): String {
        val builder = StringBuilder()
        builder.append(FeatureFeedsMicroSDK.getAppScheme())
        builder.append("://screen/feed_detail/")
        builder.append(feedType.feedType?.lowercase() ?: "")
        builder.append("?position=")
        builder.append(feedType.feedPosition ?: 0)
        builder.append("&source=")
        builder.append(feedType.dataSource?.lowercase() ?: "")
        builder.append("&feed_id=")
        builder.append(feedType.feedId ?: "")
        builder.append("&uid=")
        builder.append(mFeatureFeedModel.uid)
        //return "${FeatureFeedsMicroSDK.getAppScheme()}://screen/feed_detail/${feedType.feedType?.lowercase() ?: ""}?position=${feedType.feedPosition ?: 0}&source=${feedType.dataSource?.lowercase() ?: ""}&feed_id=${feedType.feedId ?: ""}&uid=${mFeatureFeedModel.uid}"
        return builder.toString()
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

    private fun generateCMSImageUrl(url: String?): String? {
        val imgFormat = FeatureFeedsMicroSDK.getImageFormat()
        return if ((url?.startsWith(CONTENT_STACK_URL) == true) && (!imgFormat.isNullOrEmpty())) {
            "$url?$imgFormat"
        } else {
            url
        }
    }
}