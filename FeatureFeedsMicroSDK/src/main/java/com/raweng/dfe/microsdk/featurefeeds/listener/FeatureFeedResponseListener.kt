package com.raweng.dfe.microsdk.featurefeeds.listener

import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroError

interface FeatureFeedResponseListener {
    fun onSuccess(feeds:List<FeaturedFeedModel>)
    fun onError(error:MicroError)
}