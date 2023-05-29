package com.raweng.dfe.microsdk.featurefeeds.manager

import com.contentstack.sdk.*
import com.raweng.dfe.microsdk.featurefeeds.data.FeatureFeedRepositoryImpl
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import io.reactivex.rxjava3.core.Single


class LocalApiManager(private val stack: Stack) {

    fun fetchFeatureFeed(csContentType: String): Single<MicroResult<List<FeaturedFeedModel>>> {
        val repository = FeatureFeedRepositoryImpl(stack)
        return repository.getFeatureFeeds(csContentType)
    }
}
