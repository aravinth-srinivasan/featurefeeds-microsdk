package com.raweng.dfe.microsdk.featurefeeds.data

import android.util.Log
import com.contentstack.sdk.*
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroError
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FeatureFeedRepositoryImpl(private val stack: Stack) : FeatureFeedRepository {

    override suspend fun getFeatureFeed(contentType: String): Flow<MicroResult<Any>> = flow {
        try {
            val query: Query = stack.contentType(contentType).query()
            query.setCachePolicy(CachePolicy.NETWORK_ONLY)

            val data  = suspendCancellableCoroutine<Result<QueryResult?>> {
                query.find(object : QueryResultsCallBack() {
                    override fun onCompletion(
                        responseType: ResponseType,
                        queryresult: QueryResult?,
                        error: Error?
                    ) {
                        if (error == null) {
                            it.resume(Result.success(queryresult))
                        } else {
                            it.resume(Result.failure(MicroError(error.errorMessage)))
                        }
                    }
                })
            }

            if (data.isSuccess) {
                data.onSuccess {
                    if (it !=null) {
                        for (resultObject in it.resultObjects) {
                            if (resultObject != null) {
                                val resultJson = resultObject.toJSON().toString()
                                emit(MicroResult.Success(resultJson))
                            }
                        }
                    }
                }
            }

            if (data.isFailure) {
                data.onFailure {
                    emit(MicroResult.Failure(error = MicroError(it.message ?: "")))
                }
            }



            /*val queryResult = suspendCoroutine { continuation ->
                query.find(object : QueryResultsCallBack() {
                    override fun onCompletion(
                        responseType: ResponseType,
                        queryresult: QueryResult?,
                        error: Error?
                    ) {
                        if (error == null) {
                            continuation.resumeWith(Result.success(queryresult))
                        } else {
                            continuation.resumeWith(Result.failure(MicroError(error.errorMessage)))
                        }
                    }
                })
            }

            if (queryResult != null) {
                for (resultObject in queryResult.resultObjects) {
                    if (resultObject != null) {
                        val resultJson = resultObject.toJSON().toString()
                        emit(MicroResult.Success(resultJson))
                    }
                }
            }*/

        } catch (e: Exception) {
            emit(MicroResult.Failure(error = MicroError(e.message ?: "")))
        }
    }
}
