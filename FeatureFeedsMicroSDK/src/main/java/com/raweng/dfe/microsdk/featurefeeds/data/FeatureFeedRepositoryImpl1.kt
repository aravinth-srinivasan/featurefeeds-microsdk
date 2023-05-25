package com.raweng.dfe.microsdk.featurefeeds.data

import android.util.Log
import com.contentstack.sdk.*
import com.google.gson.Gson
import com.raweng.dfe.DFEManager
import com.raweng.dfe.microsdk.featurefeeds.mapper.FeatureFeedMapper
import com.raweng.dfe.microsdk.featurefeeds.model.ContentType
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedModel
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import com.raweng.dfe.models.feed.DFEFeedCallback
import com.raweng.dfe.models.feed.DFEFeedModel
import com.raweng.dfe.modules.policy.ErrorModel
import com.raweng.dfe.modules.policy.RequestType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedModel.Entry as LocalResponseEntry

class FeatureFeedRepositoryImpl1(private val stack: Stack) : FeatureFeedRepository {

    override fun getFeatureFeeds(contentType: ContentType):
            Flowable<MicroResult<ArrayList<LocalResponseEntry>>> {
        return Flowable.create({ emitter ->
            val finalCall = fetchCMSFeeds(contentType)
                .map {
                    it.map { entry ->
                        entry.feedType?.map { feedType->
                            if (feedType?.nbaFeeds?.nbaFeeds !=null) {
                                Log.e("TAG", "getFeatureFeeds: before NBA "+feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.title, )
                            } else if (feedType?.gallery!=null) {
                                    Log.e("TAG", "getFeatureFeeds: before Gallery " + feedType.gallery?.title,)
                            } else if (feedType?.video!=null) {
                                    Log.e("TAG", "getFeatureFeeds: before Video " + feedType.video?.title,)
                            } else if (feedType?.webUrl!=null) {
                                    Log.e("TAG", "getFeatureFeeds: before weburl " + feedType.webUrl?.title,)
                            } else {
                                Log.e("TAG", "getFeatureFeeds: before no data")
                            }

                        }?: Log.e("TAG", "feederror")
                    }
                    it
                }
                .flatMap { cmsFeatureFeeds ->
                    Flowable.fromIterable(cmsFeatureFeeds)
                        .flatMap { entry ->
                            modifyFeedItem(entry)
                        }
                }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.map { entry ->
                        entry.feedType?.map { feedType->
                            if (feedType?.nbaFeeds?.nbaFeeds !=null) {
                                Log.e("TAG", "getFeatureFeeds: After NBA "+feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel?.title, )
                            } else if (feedType?.gallery!=null) {
                                Log.e("TAG", "getFeatureFeeds: After Gallery " + feedType.gallery?.title,)
                            } else if (feedType?.video!=null) {
                                Log.e("TAG", "getFeatureFeeds: After Video " + feedType.video?.title,)
                            } else if (feedType?.webUrl!=null) {
                                Log.e("TAG", "getFeatureFeeds: After weburl " + feedType.webUrl?.title,)
                            } else {
                                Log.e("TAG", "getFeatureFeeds: After no data")
                            }

                        }?: Log.e("TAG", "feederror")
                    }
                    it
                }
                .subscribe({ originalList ->
                    emitter.onNext(MicroResult.Success(ArrayList(originalList)))
                    emitter.onComplete()
                }, {
                    emitter.onError(it)
                    emitter.onComplete()
                })

        }, BackpressureStrategy.BUFFER)
    }

    private fun getFeatureFeedResponseList(queryResult: QueryResult?): ArrayList<LocalResponseEntry> {
        val tempList = arrayListOf<LocalResponseEntry>()
        queryResult?.let {
            for (resultObject in it.resultObjects) {
                if (resultObject != null) {
                    val resultJson = resultObject.toJSON().toString()
                    val finalData = Gson().fromJson(resultJson, LocalResponseEntry::class.java)
                    tempList.add(finalData)
                }
            }
        }
        return tempList
    }

    private fun fetchDFEFeeds(nid: String): Flowable<DFEFeedModel> {
        return Observable.create<DFEFeedModel> { emitter ->
            DFEManager.getInst().queryManager.getFeed(
                null,
                nid,
                RequestType.Network,
                object : DFEFeedCallback() {
                    override fun onCompletion(
                        data: MutableList<DFEFeedModel>?,
                        error: ErrorModel?
                    ) {
                        if (!data.isNullOrEmpty()) {
                            emitter.onNext(data[0])
                        }

                        if (error != null) {
                            emitter.onError(Throwable(error.errorMessage))
                        }
                        emitter.onComplete()
                    }
                }
            )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toFlowable(BackpressureStrategy.BUFFER)
    }

    private fun fetchCMSFeeds(contentType: ContentType): Flowable<ArrayList<LocalResponseEntry>> {
        return Flowable.create({ emitter ->
            try {
                val query: Query = stack.contentType(contentType.toString()).query()
                query.setCachePolicy(CachePolicy.NETWORK_ONLY)
                query.find(object : QueryResultsCallBack() {
                    override fun onCompletion(
                        responseType: ResponseType?,
                        queryresult: QueryResult?,
                        error: Error?
                    ) {
                        if (error == null) {
                            val mTempList = getFeatureFeedResponseList(queryresult)
                            emitter.onNext(mTempList)
                            emitter.onComplete()
                        } else {
                            emitter.onError(Throwable(error.errorMessage))
                            emitter.onComplete()
                        }
                    }
                })
            } catch (e: Exception) {
                emitter.onError(e)
                emitter.onComplete()
            }
        }, BackpressureStrategy.BUFFER)
    }


    private fun fetchAndModifiedFeedTypeNBAData(feedType:FeatureFeedModel.Entry.FeedType?):Maybe<FeatureFeedModel.Entry.FeedType?>? {
        val nid = feedType?.nbaFeeds?.nbaFeeds?.value ?: ""
        return if (nid.isNotEmpty()) {
            val fetchDFEFeedAndUpdatedToList=  fetchDFEFeeds(nid)
                .map { dfeFeeds ->
                    val mapper = FeatureFeedMapper(dfeFeeds)
                    feedType?.nbaFeeds?.nbaFeeds?.nbaFeedModel = mapper.getNbaFeedModel()
                    feedType
                }
            fetchDFEFeedAndUpdatedToList?.firstElement() ?: Maybe.empty()
        } else {
            Maybe.just(feedType)
        }
    }

    private fun modifyFeedItem(entry: FeatureFeedModel.Entry): Flowable<FeatureFeedModel.Entry> {
        val feedTypeList = entry.feedType ?: emptyList()
        val feedItemModification = Flowable.fromIterable(feedTypeList)
            ?.flatMapMaybe { feedType ->
                fetchAndModifiedFeedTypeNBAData(feedType)
            }
            ?.toList()
            ?.map {
                entry.feedType = it
                entry
            }
        return feedItemModification?.toFlowable() ?: Flowable.empty()
    }
}

