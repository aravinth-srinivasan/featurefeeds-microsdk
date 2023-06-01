package com.raweng.dfe.microsdk.featurefeeds

import android.content.Context
import android.util.Log
import com.contentstack.sdk.Config
import com.contentstack.sdk.Contentstack
import com.contentstack.sdk.Stack
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.manager.LocalApiManager
import com.raweng.dfe.microsdk.featurefeeds.type.DateFormat

class FeatureFeedsMicroSDK private constructor() {
    private var stack: Stack? = null
    private var localApiManager: LocalApiManager? = null

    companion object {
        private const val DEFAULT_DATE_FORMAT = "MMMM dd, yyyy"
        private val localInstance: FeatureFeedsMicroSDK by lazy { FeatureFeedsMicroSDK() }

        private var csHostUrl: String? = null
        private var csApiKey: String? =null
        private var environment: String? = null
        private var csAccessToken: String? = null
        private var appScheme: String? = null
        private var dateFormat:String = ""
        private var dateFormatType: DateFormat? = null

       internal fun getCSHostUrl():String? = csHostUrl
        internal fun getCSApiKey():String? = csApiKey
        internal fun getCSEnvironment():String? = environment
        internal fun getCSAccessToken():String? = csAccessToken
        internal fun getCSDateFormat():String = dateFormat
        internal fun getCSDateFormatType():DateFormat? = dateFormatType
        internal fun getAppScheme():String? = appScheme
        @JvmStatic
        fun getInstance(): FeatureFeedsMicroSDK = localInstance

        @JvmStatic
        fun initialize(
            context: Context,
            csHostUrl: String?,
            csApiKey: String?,
            environment: String?,
            csAccessToken: String?,
            appScheme: String?,
            dateFormatType: DateFormat?,
            dateFormat: String? = null
        ) {
            this.csHostUrl = csHostUrl
            this.csApiKey = csApiKey
            this.environment = environment
            this.csAccessToken = csAccessToken
            this.appScheme = appScheme
            this.dateFormatType = dateFormatType
            this.dateFormat = dateFormat.takeUnless { it.isNullOrEmpty() } ?: DEFAULT_DATE_FORMAT
            try {
                if (isAllFieldsAreNotEmptyOrNull()) {
                    initContentStack(context)
                    setupLocalApiManager()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("TAG", "initContentStack: Failed")
            }
        }

        private fun isAllFieldsAreNotEmptyOrNull(): Boolean {
            return listOf(csHostUrl, csApiKey, csAccessToken, environment).all { !it.isNullOrEmpty() }
        }

        private fun initContentStack(context: Context) {
            localInstance.stack = Contentstack.stack(
                context,
                csApiKey.orEmpty(),
                csAccessToken.orEmpty(),
                environment.orEmpty(),
                Config().apply {
                    host = csHostUrl
                }
            )
        }

        private fun setupLocalApiManager() {
            localInstance.stack?.run {
                localInstance.localApiManager =
                    LocalApiManager(this)
            } ?: Log.e("TAG", "please init content stack")
        }
    }

    fun getFeatureFeed(
        csContentType: String,
        responseListener: FeatureFeedResponseListener
    ) {
        localApiManager?.fetchFeatureFeed(csContentType, responseListener)
    }
}
