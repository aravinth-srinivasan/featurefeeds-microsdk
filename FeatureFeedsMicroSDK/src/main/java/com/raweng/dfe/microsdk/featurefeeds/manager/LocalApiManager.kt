package com.raweng.dfe.microsdk.featurefeeds.manager

import com.contentstack.sdk.*
import com.raweng.dfe.microsdk.featurefeeds.data.FeatureFeedRepository
import com.raweng.dfe.microsdk.featurefeeds.data.FeatureFeedRepositoryImpl
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel


class LocalApiManager(stack: Stack) {
    private var repository: FeatureFeedRepository = FeatureFeedRepositoryImpl(stack)

    fun fetchFeatureFeed(
        csContentType: String,
        responseListener: FeatureFeedResponseListener
    ) {
        repository.getFeatureFeeds(csContentType, responseListener)
    }

    fun getFeaturedFeedsModel(uid: String?): FeaturedFeedModel? {
        return repository.getFeaturedFeedsModel(uid)
    }
}
