package com.raweng.dfe.microsdk.featurefeeds.data

import android.annotation.SuppressLint
import com.contentstack.sdk.*
import com.google.gson.Gson
import com.raweng.dfe.microsdk.featurefeeds.mapper.FeaturedFeedsMapper
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import io.reactivex.rxjava3.core.*
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse.Entry as LocalResponseEntry

internal class FeatureFeedRepositoryImpl(private val stack: Stack) : FeatureFeedRepository {

    override fun getFeatureFeeds(csContentType: String): Single<MicroResult<List<FeaturedFeedModel>>> {
        return fetchCMSFeeds(csContentType)
    }

    private fun fetchCMSFeeds(contentType: String): Single<MicroResult<List<FeaturedFeedModel>>> {
        return Single.create { emitter ->
            try {
                val query: Query = stack.contentType(contentType).query()
                query.setCachePolicy(CachePolicy.NETWORK_ONLY)
                query.find(object : QueryResultsCallBack() {
                    @SuppressLint("CheckResult")
                    override fun onCompletion(
                        responseType: ResponseType?,
                        queryresult: QueryResult?,
                        error: Error?
                    ) {
                        if (error == null) {
                            val finalList = getFeatureFeedResponseList(queryresult)
                            emitter.onSuccess(MicroResult.Success(finalList))
                        } else {
                            emitter.onError(Throwable(error.errorMessage))
                        }
                    }
                })
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private fun getFeatureFeedResponseList(queryResult: QueryResult?): List<FeaturedFeedModel> {
        return queryResult?.resultObjects?.mapNotNull { resultObject ->
            val resultJson = resultObject.toJSON().toString()
            val finalData = Gson().fromJson(resultJson, LocalResponseEntry::class.java)
            FeaturedFeedsMapper(finalData).getFeatureFeeds()
        } ?: emptyList()
    }
}

