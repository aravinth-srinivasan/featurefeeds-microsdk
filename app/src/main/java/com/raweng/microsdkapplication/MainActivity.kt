package com.raweng.microsdkapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.raweng.dfe.DFEManager
import com.raweng.dfe.microsdk.featurefeeds.FeatureFeedsMicroSDK
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroError
import com.raweng.dfe.models.config.DFEConfigCallback
import com.raweng.dfe.models.config.DFEConfigModel
import com.raweng.dfe.modules.policy.ErrorModel
import com.raweng.dfe.modules.policy.RequestType
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var csApiKey = ""
    var csAccessToken = ""
    var csEnv = ""
    var csUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchConfigAndInitMicroSDK()
        findViewById<AppCompatButton>(R.id.btnFetch).setOnClickListener {
            fetchFeatureFeed()
        }
    }

    private fun fetchConfigAndInitMicroSDK() {
        DFEManager.getInst().queryManager.getConfig(
            "",
            RequestType.Network,
            object : DFEConfigCallback() {
                override fun onCompletion(apiData: MutableList<DFEConfigModel>?, p1: ErrorModel?) {
                    if (!apiData.isNullOrEmpty()) {
                        prepareData(apiData)
                        FeatureFeedsMicroSDK.init(
                            this@MainActivity,
                            "",
                            "",
                            csUrl = csUrl,
                            csApiKey = csApiKey,
                            csAccessToken = csAccessToken,
                            csEnv = csEnv,
                        )
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
        findViewById<TextView>(R.id.textV).text = "Please wait fetching..."
        FeatureFeedsMicroSDK.getInstance()
            .getFeatureFeed("featured_feeds", object : FeatureFeedResponseListener {
                override fun onSuccess(feeds: List<FeaturedFeedModel>) {
                    Log.e("TAG", "onSuccess: " + feeds.size)
                    val builder = StringBuilder()
                    feeds.forEach {
                        builder.append("Title : ${it.title} - Order: ${it.order}")
                        builder.append("\n")
                    }
                    findViewById<TextView>(R.id.textV).text = builder.toString()
                }

                override fun onError(error: MicroError) {
                    Log.e("TAG", "onError: " + error.errorMsg)
                    findViewById<TextView>(R.id.textV).text = error.errorMsg
                }
            })
    }
}