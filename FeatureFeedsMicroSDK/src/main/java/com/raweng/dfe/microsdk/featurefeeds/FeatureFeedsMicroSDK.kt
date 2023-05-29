package com.raweng.dfe.microsdk.featurefeeds

import android.content.Context
import android.util.Log
import com.contentstack.sdk.Contentstack
import com.contentstack.sdk.Stack
import com.raweng.dfe.microsdk.featurefeeds.manager.LocalApiManager
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import io.reactivex.rxjava3.core.Single

class FeatureFeedsMicroSDK private constructor() {
    private var stack: Stack? = null
    private var localApiManager: LocalApiManager? = null

    companion object {
        private val localInstance: FeatureFeedsMicroSDK by lazy { FeatureFeedsMicroSDK() }

        @JvmStatic
        fun getInstance(): FeatureFeedsMicroSDK = localInstance

        @JvmStatic
        fun init(
            context: Context,
            dfeSportsKey: String?,
            dfeEnv: String?,
            csApiKey: String?,
            csAccessToken: String?,
            csEnv: String?
        ) {
            try {
                if (isAllFieldsAreNotEmptyOrNull(csApiKey, csAccessToken, csEnv)) {
                    initContentStack(context, csApiKey, csAccessToken, csEnv)
                    setupLocalApiManager()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("TAG", "initContentStack: Failed")
            }
        }

        private fun isAllFieldsAreNotEmptyOrNull(
            csApiKey: String?,
            csAccessToken: String?,
            csEnv: String?
        ): Boolean {
            return listOf(csApiKey, csAccessToken, csEnv).all { !it.isNullOrEmpty() }
        }

        private fun initContentStack(
            context: Context,
            csApiKey: String?,
            csAccessToken: String?,
            csEnv: String?
        ) {
            localInstance.stack = Contentstack.stack(
                context,
                csApiKey.orEmpty(),
                csAccessToken.orEmpty(),
                csEnv.orEmpty()
            )
        }

        private fun setupLocalApiManager() {
            localInstance.stack?.run {
                localInstance.localApiManager = LocalApiManager(this)
            } ?: Log.e("TAG", "please init content stack")
        }
    }

    fun getFeatureFeed(csContentType: String): Single<MicroResult<List<FeaturedFeedModel>>>? {
        return localApiManager?.fetchFeatureFeed(csContentType)
    }
}
