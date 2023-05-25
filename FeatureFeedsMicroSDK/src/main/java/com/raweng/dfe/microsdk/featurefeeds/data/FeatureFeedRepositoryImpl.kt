package com.raweng.dfe.microsdk.featurefeeds.data

import com.contentstack.sdk.*
import com.google.gson.Gson
import com.raweng.dfe.DFEManager
import com.raweng.dfe.microsdk.featurefeeds.mapper.FeatureFeedMapper
import com.raweng.dfe.microsdk.featurefeeds.model.ContentType
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import com.raweng.dfe.models.feed.DFEFeedCallback
import com.raweng.dfe.models.feed.DFEFeedModel
import com.raweng.dfe.modules.policy.ErrorModel
import com.raweng.dfe.modules.policy.RequestType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedModel.Entry as LocalResponseEntry

class FeatureFeedRepositoryImpl(private val stack: Stack) : FeatureFeedRepository {

    override fun getFeatureFeeds(contentType: ContentType):
            Flowable<MicroResult<ArrayList<LocalResponseEntry>>> {
        return Flowable.create({ emitter ->
            val finalCall = Flowable.zip(
                fetchCMSFeeds(contentType),
                fetchDFEFeeds()
            ) { featureFeedsResult: ArrayList<LocalResponseEntry>, fetchedFeeds: ArrayList<DFEFeedModel> ->
                Pair(featureFeedsResult, fetchedFeeds)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { (cmsFeatureFeeds, dfeFeeds) ->
                    modifyFeatureFeeds(cmsFeatureFeeds, dfeFeeds)
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

    private fun fetchDFEFeeds(): Flowable<ArrayList<DFEFeedModel>> {
        return Observable.create<ArrayList<DFEFeedModel>> { emitter ->
            DFEManager.getInst().queryManager.getFeeds(
                null,
                null,
                null,
                RequestType.Network,
                object : DFEFeedCallback() {
                    override fun onCompletion(
                        data: MutableList<DFEFeedModel>?,
                        error: ErrorModel?
                    ) {
                        if (data != null) {
                            emitter.onNext(ArrayList(data))
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

    private fun modifyFeatureFeeds(
        cmsFeatureFeeds: List<LocalResponseEntry>,
        dfeFeeds: List<DFEFeedModel>
    ): Flowable<List<LocalResponseEntry>> {
        return Flowable.fromIterable(cmsFeatureFeeds)
            .filter { entry -> entry.feedType?.any { it?.nbaFeeds?.nbaFeeds?.value != null } == true }
            .doOnNext { entry ->
                entry.feedType?.forEach { feedType ->
                    if (feedType?.nbaFeeds?.nbaFeeds?.value != null) {
                        val matchingFeed = dfeFeeds.firstOrNull { feed ->
                            feed.newsid == feedType.nbaFeeds?.nbaFeeds?.value
                        }
                        if (matchingFeed != null) {
                            val mapper = FeatureFeedMapper(matchingFeed)
                            feedType.nbaFeeds?.nbaFeeds?.nbaFeedModel = mapper.getNbaFeedModel()
                        }
                    }
                }
            }
            .toList()
            .map { modifiedEntries ->
                cmsFeatureFeeds
            }
            .toFlowable()
    }

}

