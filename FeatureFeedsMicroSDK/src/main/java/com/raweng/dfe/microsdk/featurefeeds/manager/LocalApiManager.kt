package com.raweng.dfe.microsdk.featurefeeds.manager

import android.util.Log
import com.contentstack.sdk.*
import com.raweng.dfe.microsdk.featurefeeds.data.FeatureFeedRepositoryImpl
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class LocalApiManager(private val stack: Stack) {

    suspend fun fetchFeatureFeed(contentType: String):Flow<MicroResult<Any>> = flow{
        val featureFeed = FeatureFeedRepositoryImpl(stack)
        featureFeed.getFeatureFeed(contentType).collect {
            emit(it)
        }
    }
}
