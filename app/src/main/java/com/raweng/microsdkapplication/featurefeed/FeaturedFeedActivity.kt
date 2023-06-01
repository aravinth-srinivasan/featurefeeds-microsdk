package com.raweng.microsdkapplication.featurefeed

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.raweng.dfe.DFEManager
import com.raweng.dfe.microsdk.featurefeeds.FeatureFeedsMicroSDK
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.type.DateFormat
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroError
import com.raweng.dfe.models.config.DFEConfigCallback
import com.raweng.dfe.models.config.DFEConfigModel
import com.raweng.dfe.modules.policy.ErrorModel
import com.raweng.dfe.modules.policy.RequestType
import com.raweng.dfe_components_android.components.featuredfeeds.FeaturedFeedsView
import com.raweng.dfe_components_android.components.featuredfeeds.model.FeaturedFeedsViewDataModel
import com.raweng.microsdkapplication.R
import org.json.JSONObject

class FeaturedFeedActivity : AppCompatActivity() {
    var csApiKey = ""
    var csAccessToken = ""
    var csEnv = ""
    var csUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_featured_feed)
        fetchConfigAndInitMicroSDK()
    }

    private fun fetchConfigAndInitMicroSDK() {
        DFEManager.getInst().queryManager.getConfig(
            "",
            RequestType.Network,
            object : DFEConfigCallback() {
                override fun onCompletion(apiData: MutableList<DFEConfigModel>?, p1: ErrorModel?) {
                    if (!apiData.isNullOrEmpty()) {
                        prepareData(apiData)
                        FeatureFeedsMicroSDK.initialize(
                            this@FeaturedFeedActivity,
                            csHostUrl = csUrl,
                            csApiKey = csApiKey,
                            csAccessToken = csAccessToken,
                            environment = csEnv,
                            appScheme = "bulls",
                            dateFormatType = DateFormat.HOURS_AGO,
                            dateFormat = "MM/dd/yyyy"
                        )
                        fetchFeatureFeed()
                    } else {
                        Log.e("TAG", "onCompletion: " + p1.toString())
                    }
                }
            })
    }


    private fun prepareData(apiData: MutableList<DFEConfigModel>) {
        val mConfigModel = apiData[0]
        val data = JSONObject(mConfigModel.integrations)
        if (data.has("contentstack")) {
            val contentStack = data.getJSONObject("contentstack")
            if (contentStack.has("app_key")) {
                csApiKey = contentStack.getString("app_key")
            }

            if (contentStack.has("environment")) {
                csEnv = contentStack.getString("environment")
            }
            if (contentStack.has("delivery_token")) {
                csAccessToken =
                    contentStack.getString("delivery_token")
            }
            if (contentStack.has("url")) {
                csUrl = contentStack.getString("url")
            }
        }
    }

    private fun fetchFeatureFeed() {
        FeatureFeedsMicroSDK.getInstance()
            .getFeatureFeed("featured_feeds", object : FeatureFeedResponseListener {
                override fun onSuccess(feeds: List<FeaturedFeedModel>) {
                    Log.e("TAG", "onSuccess: " + feeds.size)
                    val mapper = FeaturedFeedDataMapper(feeds)
                    mapper.getFeaturedFeedList()
                    findViewById<FeaturedFeedsView>(R.id.featuredFeedView).apply {
                        configureView(
                            style = "featuredFeedsViewTheme",
                            data = FeaturedFeedsViewDataModel(
                                featuredFeeds = mapper.getFeaturedFeedList()
                            )
                        )
                    }
                }

                override fun onError(error: MicroError) {
                    Log.e("TAG", "onError: " + error.errorMsg)
                }
            })
    }

}