package com.raweng.dfe.microsdk.featurefeeds.data

import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import io.reactivex.rxjava3.core.Single

interface FeatureFeedRepository {
    fun getFeatureFeeds(csContentType: String): Single<MicroResult<List<FeaturedFeedModel>>>

}