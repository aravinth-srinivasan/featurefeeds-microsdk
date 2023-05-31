package com.raweng.dfe.microsdk.featurefeeds

import android.content.Context
import android.util.Log
import com.contentstack.sdk.Config
import com.contentstack.sdk.Contentstack
import com.contentstack.sdk.Stack
import com.raweng.dfe.DFEManager
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.manager.LocalApiManager
import com.raweng.dfe.microsdk.featurefeeds.type.DateFormat

class FeatureFeedsMicroSDK private constructor() {
    private var stack: Stack? = null
    private var localApiManager: LocalApiManager? = null

    companion object {
        private const val DEFAULT_DATE_FORMAT = "MMMM dd, yyyy"
        private val localInstance: FeatureFeedsMicroSDK by lazy { FeatureFeedsMicroSDK() }
        private var dateFormat:String = ""
        private var appScheme: String? = null
        private var dateFormatType: DateFormat? = null

        @JvmStatic
        fun getInstance(): FeatureFeedsMicroSDK = localInstance

        @JvmStatic
        fun initialize(
            context: Context,
            csUrl: String?,
            csApiKey: String?,
            csAccessToken: String?,
            csEnv: String?,
            appScheme: String?,
            dateFormatType: DateFormat?,
            dateFormat: String? = null
        ) {
            this.dateFormat = dateFormat.takeUnless { it.isNullOrEmpty() } ?: DEFAULT_DATE_FORMAT
            this.appScheme = appScheme
            this.dateFormatType = dateFormatType
            try {
                if (isAllFieldsAreNotEmptyOrNull(csUrl, csApiKey, csAccessToken, csEnv)
                ) {
                    initContentStack(context, csUrl, csApiKey, csAccessToken, csEnv)
                    setupLocalApiManager()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("TAG", "initContentStack: Failed")
            }
        }

        private fun isAllFieldsAreNotEmptyOrNull(
            csUrl: String?,
            csApiKey: String?,
            csAccessToken: String?,
            csEnv: String?
        ): Boolean {
            return listOf(csUrl, csApiKey, csAccessToken, csEnv).all { !it.isNullOrEmpty() }
        }

        private fun initContentStack(
            context: Context,
            csUrl: String?,
            csApiKey: String?,
            csAccessToken: String?,
            csEnv: String?
        ) {
            localInstance.stack = Contentstack.stack(
                context,
                csApiKey.orEmpty(),
                csAccessToken.orEmpty(),
                csEnv.orEmpty(),
                Config().apply {
                    host = csUrl
                }
            )
            //DFEManager.init(context)
        }

        private fun setupLocalApiManager() {
            localInstance.stack?.run {
                localInstance.localApiManager =
                    LocalApiManager(appScheme, dateFormatType, dateFormat, this)
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
