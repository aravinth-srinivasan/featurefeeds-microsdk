package com.raweng.dfe.microsdk.featurefeeds.data

import kotlinx.coroutines.flow.Flow

interface FeatureFeedRepository {
    suspend fun getFeatureFeed(contentType: String): Flow<Any>
}