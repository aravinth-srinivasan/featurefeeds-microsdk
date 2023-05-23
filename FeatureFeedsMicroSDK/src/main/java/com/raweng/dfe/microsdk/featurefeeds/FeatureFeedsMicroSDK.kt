package com.raweng.dfe.microsdk.featurefeeds

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.contentstack.sdk.Config
import com.contentstack.sdk.Contentstack
import com.contentstack.sdk.Stack
import com.raweng.dfe.microsdk.featurefeeds.manager.LocalApiManager
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import kotlinx.coroutines.flow.Flow
import com.raweng.dfe.microsdk.featurefeeds.model.ConfigModel as LocalConfig

class FeatureFeedsMicroSDK private constructor() {
    private var mStack: Stack? = null
    private var mLocalApiManager: LocalApiManager? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: FeatureFeedsMicroSDK? = null

        fun getInstance(): FeatureFeedsMicroSDK {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = FeatureFeedsMicroSDK()
                    }
                }
            }
            return instance!!
        }

        fun init(context: Context, config: LocalConfig) {
            try {
                config.apply {
                    val mConfigHost = Config()
                    mConfigHost.host = cmsUrl
                    val allFieldsNotEmptyOrNull = listOf(
                        cmsApiKey,
                        cmsAccessToken,
                        environment,
                        cmsUrl
                    ).all { !it.isNullOrEmpty() }

                    if (allFieldsNotEmptyOrNull) {
                        getInstance().mStack = Contentstack.stack(
                            context,
                            cmsApiKey ?: "",
                            cmsAccessToken ?: "",
                            environment ?: "",
                            mConfigHost
                        )

                        getInstance().mStack?.let {
                            getInstance().mLocalApiManager = LocalApiManager(it)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("TAG", "initContentStack: Failed")
            }
        }
    }


    suspend fun getFeatureFeed(contentType: String): Flow<MicroResult<Any>>? {
        return getInstance().mLocalApiManager?.fetchFeatureFeed(contentType)
    }
}