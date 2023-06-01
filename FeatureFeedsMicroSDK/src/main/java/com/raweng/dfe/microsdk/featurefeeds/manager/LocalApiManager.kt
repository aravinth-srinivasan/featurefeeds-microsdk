package com.raweng.dfe.microsdk.featurefeeds.manager

import com.contentstack.sdk.*
import com.raweng.dfe.microsdk.featurefeeds.FeatureFeedsMicroSDK
import com.raweng.dfe.microsdk.featurefeeds.data.FeatureFeedRepositoryImpl
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.type.DateFormat


class LocalApiManager(private val stack: Stack) {

    fun fetchFeatureFeed(
        csContentType: String,
        responseListener: FeatureFeedResponseListener
    ) {
        val repository = FeatureFeedRepositoryImpl(stack)
        repository.getFeatureFeeds(csContentType, responseListener)
    }
}
