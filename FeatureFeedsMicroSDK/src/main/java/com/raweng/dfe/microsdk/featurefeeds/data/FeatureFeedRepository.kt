package com.raweng.dfe.microsdk.featurefeeds.data

import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener

interface FeatureFeedRepository {
    fun getFeatureFeeds(csContentType: String, responseListener: FeatureFeedResponseListener)

}