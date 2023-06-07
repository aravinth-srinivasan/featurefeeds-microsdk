package com.raweng.dfe.microsdk.featurefeeds.data

import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel

interface FeatureFeedRepository {
    fun getFeatureFeeds(csContentType: String, responseListener: FeatureFeedResponseListener)
    fun getFeaturedFeedsModel(uid: String?): FeaturedFeedModel?

}