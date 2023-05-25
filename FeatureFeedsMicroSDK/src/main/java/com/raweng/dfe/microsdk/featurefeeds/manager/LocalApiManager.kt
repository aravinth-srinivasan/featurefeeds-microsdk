package com.raweng.dfe.microsdk.featurefeeds.manager

import com.contentstack.sdk.*
import com.raweng.dfe.microsdk.featurefeeds.data.FeatureFeedRepositoryImpl
import com.raweng.dfe.microsdk.featurefeeds.data.FeatureFeedRepositoryImpl1
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import io.reactivex.rxjava3.core.Flowable
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedModel.Entry as LocalResponseEntry


class LocalApiManager(private val stack: Stack) {

    fun fetchFeatureFeed(contentType: String): Flowable<MicroResult<ArrayList<LocalResponseEntry>>> {
        val repository = FeatureFeedRepositoryImpl1(stack)
        return repository.getFeatureFeeds(contentType)
    }
}
