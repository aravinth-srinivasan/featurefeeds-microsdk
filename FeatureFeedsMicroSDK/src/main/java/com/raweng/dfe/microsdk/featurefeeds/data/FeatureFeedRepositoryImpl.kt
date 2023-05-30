package com.raweng.dfe.microsdk.featurefeeds.data

import android.annotation.SuppressLint
import com.contentstack.sdk.*
import com.google.gson.Gson
import com.raweng.dfe.DFEManager
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.mapper.DFENBAFeedMapper
import com.raweng.dfe.microsdk.featurefeeds.mapper.FeaturedFeedsMapper
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroError
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import com.raweng.dfe.models.feed.DFEFeedCallback
import com.raweng.dfe.models.feed.DFEFeedModel
import com.raweng.dfe.modules.policy.ErrorModel
import com.raweng.dfe.modules.policy.RequestType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse.Entry as LocalResponseEntry

internal class FeatureFeedRepositoryImpl(private val stack: Stack) : FeatureFeedRepository {

    override fun getFeatureFeeds(
        csContentType: String,
        responseListener: FeatureFeedResponseListener
    ) {
        try {
            val result = fetchCMSFeeds(csContentType)
            result.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    when (it) {
                        is MicroResult.Failure -> {
                            responseListener.onError(it.error)
                        }
                        is MicroResult.Success -> {
                            responseListener.onSuccess(it.data)
                        }
                    }
                }, { error ->
                    responseListener.onError(MicroError(error.message ?: ""))
                })
        } catch (e: Exception) {
            responseListener.onError(MicroError(e.message ?: "", exception = e))
        }
    }


    private fun fetchCMSFeeds(contentType: String): Flowable<MicroResult<List<FeaturedFeedModel>>> {
        return Flowable.defer {
            val query: Query = stack.contentType(contentType).query()
            query.setCachePolicy(CachePolicy.NETWORK_ONLY)

            Flowable.create<MicroResult<List<FeaturedFeedModel>>>({ emitter ->
                query.find(object : QueryResultsCallBack() {
                    @SuppressLint("CheckResult")
                    override fun onCompletion(
                        responseType: ResponseType?,
                        queryresult: QueryResult?,
                        error: Error?
                    ) {
                        if (error == null) {
                            getFeatureFeedResponseList(queryresult)
                                .subscribe({ finalList ->
                                    emitter.onNext(MicroResult.Success(finalList))
                                    emitter.onComplete()
                                }, {
                                    emitter.onError(Throwable(it.message))
                                    emitter.onComplete()
                                })
                        } else {
                            emitter.onError(Throwable(error.errorMessage))
                            emitter.onComplete()
                        }
                    }
                })
            }, BackpressureStrategy.BUFFER)
        }.subscribeOn(Schedulers.io())
    }

    private fun getFeatureFeedResponseList(queryResult: QueryResult?): Flowable<List<FeaturedFeedModel>> {
        return if (queryResult != null && !queryResult.resultObjects.isNullOrEmpty()) {
            Flowable.defer {
                Flowable.fromIterable(queryResult.resultObjects).flatMap { resultObject ->
                    val resultJson = resultObject.toJSON().toString()
                    val finalData = Gson().fromJson(resultJson, LocalResponseEntry::class.java)
                    val feedTypeList = finalData.feedType ?: emptyList()
                    Flowable.fromIterable(feedTypeList)
                        .flatMapMaybe { feedType ->
                            fetchAndModifiedFeedTypeNBAData(feedType)
                        }
                        .toList()
                        .doOnSuccess { modifiedFeedTypeList ->
                            finalData.feedType = modifiedFeedTypeList
                        }
                        .toFlowable()
                        ?.flatMap {
                            val mapper = FeaturedFeedsMapper(featuredFeeds = finalData)
                            Flowable.just(mapper.getFeatureFeeds())
                        }
                }
                    .toList()
                    .toFlowable()
            }.subscribeOn(Schedulers.io())
        } else {
            Flowable.just(emptyList())
        }
    }


    private fun fetchDFEFeeds(nid: String): Flowable<DFEFeedModel> {
        return Flowable.create({ emitter ->
            DFEManager.getInst().queryManager.getFeed(
                getFields(),
                nid,
                RequestType.Network,
                object : DFEFeedCallback() {
                    override fun onCompletion(
                        data: MutableList<DFEFeedModel>?,
                        error: ErrorModel?
                    ) {
                        if (data != null && data.isNotEmpty()) {
                            emitter.onNext(data[0])
                        }

                        if (error != null) {
                            emitter.onError(Throwable(error.errorMessage))
                        }
                        emitter.onComplete()
                    }
                }
            )
        }, BackpressureStrategy.BUFFER)
    }


    private fun fetchAndModifiedFeedTypeNBAData(feedType: FeatureFeedResponse.Entry.FeedType?): Maybe<FeatureFeedResponse.Entry.FeedType?> {
        val nid = feedType?.nbaFeeds?.nbaFeeds?.value ?: ""
        return if (nid.isNotEmpty()) {
            val fetchDFEFeedAndUpdatedToList = fetchDFEFeeds(nid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { dfeFeeds ->
                    val mapper = DFENBAFeedMapper(dfeFeeds)
                    feedType?.nbaFeeds?.nbaFeeds?.nbaFeedModel = mapper.getNbaFeedModel()
                    feedType
                }
            fetchDFEFeedAndUpdatedToList?.firstElement() ?: Maybe.empty()
        } else {
            Maybe.just(feedType)
        }
    }

    private fun getFields(): String {
        return "uid,nid,title,published_date,feed_type,category,media{thumbnail}"
    }
}

