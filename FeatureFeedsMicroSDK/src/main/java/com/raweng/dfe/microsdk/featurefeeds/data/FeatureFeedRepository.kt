package com.raweng.dfe.microsdk.featurefeeds.data

import com.raweng.dfe.microsdk.featurefeeds.model.ContentType
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedModel.Entry
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import io.reactivex.rxjava3.core.Flowable

interface FeatureFeedRepository {
    fun getFeatureFeeds(contentType: ContentType): Flowable<MicroResult<ArrayList<Entry>>>

}