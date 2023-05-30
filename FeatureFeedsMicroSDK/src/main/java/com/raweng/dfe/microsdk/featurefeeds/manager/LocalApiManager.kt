package com.raweng.dfe.microsdk.featurefeeds.manager

import com.contentstack.sdk.*
import com.raweng.dfe.microsdk.featurefeeds.data.FeatureFeedRepositoryImpl
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener


internal class LocalApiManager(private val stack: Stack) {

    fun fetchFeatureFeed(csContentType: String, responseListener: FeatureFeedResponseListener) {
        val repository = FeatureFeedRepositoryImpl(stack)
        return repository.getFeatureFeeds(csContentType, responseListener)
    }
}
