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
import com.raweng.dfe.microsdk.featurefeeds.utils.Utils
import com.raweng.dfe.models.feed.DFEFeedCallback
import com.raweng.dfe.models.feed.DFEFeedModel
import com.raweng.dfe.modules.policy.ErrorModel
import com.raweng.dfe.modules.policy.RequestType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse.Entry as LocalResponseEntry

class FeatureFeedRepositoryImpl(private val stack: Stack) : FeatureFeedRepository {

    private var feedList = listOf<FeaturedFeedModel>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    override fun getFeatureFeeds(
        csContentType: String,
        responseListener: FeatureFeedResponseListener
    ) {
        fetchAndGenerateList(csContentType, responseListener)
    }

    override fun getFeaturedFeedsModel(uid: String?): FeaturedFeedModel? {
        return feedList.firstOrNull { (it.uid == uid) }
    }


    private fun fetchAndGenerateList(
        csContentType: String,
        responseListener: FeatureFeedResponseListener
    ) {
        val job = coroutineScope.launch {
            var isError = false
            try {
                val response = fetchCMSFeeds(csContentType).catch {
                    isError = true
                    withContext(Dispatchers.Main) {
                        responseListener.onError(MicroError(errorMsg = it.message ?: ""))
                    }
                }.firstOrNull()

                if (!isError) {
                    val generatedList = getFeatureFeedResponseList(response)
                    withContext(Dispatchers.Main) {
                        feedList = generatedList
                        responseListener.onSuccess(generatedList)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    responseListener.onError(MicroError(errorMsg = e.message ?: ""))
                }
            }
        }
        job.invokeOnCompletion {
            coroutineScope.cancel()
        }
    }

    private suspend fun getFeatureFeedResponseList(queryResult: QueryResult?): List<FeaturedFeedModel> {
        return if (queryResult != null && !queryResult.resultObjects.isNullOrEmpty()) {
            val modifiedList = queryResult.resultObjects.mapIndexed { _, entry ->
                val resultJson = entry?.toJSON().toString()
                val finalData = Gson().fromJson(resultJson, LocalResponseEntry::class.java)
                val feedTypeList = finalData.feedType ?: emptyList()
                val modifiedFeedTypeList =
                    feedTypeList.mapIndexed { feedIndex: Int, feedType: FeatureFeedResponse.Entry.FeedType? ->
                        fetchAndModifiedFeedTypeNBAData(feedIndex, feedType)
                    }
                finalData.feedType = modifiedFeedTypeList
                val mapper = FeaturedFeedsMapper(featuredFeeds = finalData)
                mapper.getFeatureFeeds()
            }
            val sortedList = modifiedList.sortedWith(compareBy { item ->
                if (item.order != null) {
                    item.order
                } else {
                    ((-1) * (item.isoDate?.let { Utils.parseDate(it)?.time } ?: 0))
                }
            })
            sortedList
        } else {
            emptyList()
        }
    }


    private suspend fun fetchAndModifiedFeedTypeNBAData(
        position: Int,
        feedType: FeatureFeedResponse.Entry.FeedType?
    ): FeatureFeedResponse.Entry.FeedType? {
        val nid = feedType?.nbaFeeds?.nbaFeeds?.value ?: ""
        updateFeedPosition(position, feedType)
        return if (nid.isNotEmpty()) {
            try {
                val dfeFeeds = fetchDFEFeeds(nid).firstOrNull()
                if (dfeFeeds != null) {
                    val mapper = DFENBAFeedMapper(position, dfeFeeds)
                    feedType?.nbaFeeds?.nbaFeeds?.nbaFeedModel = mapper.getNbaFeedModel()
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            feedType
        } else {
            feedType
        }
    }

    private fun updateFeedPosition(position: Int, feedType: FeatureFeedResponse.Entry.FeedType?) {
        when {
            feedType?.article != null -> feedType.article?.position = position
            feedType?.video != null -> feedType.video?.position = position
            feedType?.gallery != null -> feedType.gallery?.position = position
            feedType?.webUrl != null -> feedType.webUrl?.position = position
        }
    }


    private suspend fun fetchCMSFeeds(
        csContentType: String
    ): Flow<QueryResult?> = flow {
        val queryResult = suspendCancellableCoroutine { continuation ->
            val query: Query = stack.contentType(csContentType).query()
            query.setCachePolicy(CachePolicy.NETWORK_ONLY)
            query.find(object : QueryResultsCallBack() {
                @SuppressLint("CheckResult")
                override fun onCompletion(
                    responseType: ResponseType?,
                    queryresult: QueryResult?,
                    error: Error?
                ) {
                    if (queryresult != null) {
                        continuation.resumeWith(Result.success(queryresult))
                    } else if (error != null) {
                        continuation.resumeWith(Result.failure(Throwable(error.errorMessage)))
                    } else {
                        continuation.resumeWith(Result.success(null))
                    }
                }
            })
        }
        emit(queryResult)
    }.flowOn(Dispatchers.IO)

    private fun fetchDFEFeeds(nid: String): Flow<DFEFeedModel?> = flow {
        val dfeFeedModel = suspendCancellableCoroutine { continuation ->
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
                            continuation.resumeWith(Result.success(data[0]))
                        } else if (error != null) {
                            continuation.resumeWith(Result.failure(Throwable(error.errorMessage)))
                        } else {
                            continuation.resumeWith(Result.success(null))
                        }
                    }
                }
            )
        }
        emit(dfeFeedModel)
    }.flowOn(Dispatchers.IO)

    private fun getFields(): String {
        return "uid,nid,title,published_date,feed_type,category," +
                "media{thumbnail,source,caption, type, portrait}," +
                "content,additional_content,web_url," +
                "author { name organization author_photo is_nba_staff description featured_author }"
    }

}