package com.raweng.dfe.microsdk.featurefeeds.manager

import com.contentstack.sdk.*
import com.raweng.dfe.microsdk.featurefeeds.data.FeatureFeedRepositoryImpl
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.type.DateFormat


internal class LocalApiManager(
    private val appScheme: String?,
    private val dateFormatType: DateFormat?,
    private val dateFormat: String,
    private val stack: Stack
) {

    fun fetchFeatureFeed(
        csContentType: String,
        responseListener: FeatureFeedResponseListener
    ) {
        val repository = FeatureFeedRepositoryImpl(appScheme, dateFormatType, dateFormat, stack)
        repository.getFeatureFeeds(csContentType, responseListener)
    }
}
