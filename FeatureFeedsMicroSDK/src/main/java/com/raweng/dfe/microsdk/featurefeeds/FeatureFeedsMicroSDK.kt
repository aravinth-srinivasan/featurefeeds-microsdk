package com.raweng.dfe.microsdk.featurefeeds

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.contentstack.sdk.Config
import com.contentstack.sdk.Contentstack
import com.contentstack.sdk.Stack
import com.raweng.dfe.DFEManager
import com.raweng.dfe.microsdk.featurefeeds.manager.LocalApiManager
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedModel.Entry as LocalResponseEntry
import com.raweng.dfe.microsdk.featurefeeds.model.ConfigModel as LocalConfig

class FeatureFeedsMicroSDK private constructor() {
    private var mStack: Stack? = null
    private var mLocalApiManager: LocalApiManager? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: FeatureFeedsMicroSDK? = null

        @JvmStatic
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

        @JvmStatic
        fun init(context: Context, config: LocalConfig) {
            try {
                config.apply {
                    val mConfigHost = Config()
                    mConfigHost.host = cmsUrl
                    if (isAllFieldsAreNotEmptyOrNull(this)) {
                        initContentStack(context, config, mConfigHost)
                        setupLocalApiManager()
                    }
                }
                DFEManager.init(context)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("TAG", "initContentStack: Failed")
            }
        }


        private fun isAllFieldsAreNotEmptyOrNull(config: LocalConfig): Boolean {
            return listOf(
                config.cmsApiKey,
                config.cmsAccessToken,
                config.environment,
                config.cmsUrl
            ).all { !it.isNullOrEmpty() }
        }

        private fun initContentStack(context: Context, config: LocalConfig, hostConfig: Config) {
            getInstance().mStack = Contentstack.stack(
                context,
                config.cmsApiKey ?: "",
                config.cmsAccessToken ?: "",
                config.environment ?: "",
                hostConfig
            )
        }

        private fun setupLocalApiManager() {
            getInstance().apply {
                this.mStack?.let {
                    mLocalApiManager = LocalApiManager(it)
                }
            }
        }
    }

    fun getFeatureFeed(contentType: String): Flowable<MicroResult<ArrayList<LocalResponseEntry>>>? {
        return mLocalApiManager?.fetchFeatureFeed(contentType)
    }
}