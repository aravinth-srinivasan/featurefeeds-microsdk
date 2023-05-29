package com.raweng.microsdkapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.raweng.dfe.DFEManager
import com.raweng.dfe.microsdk.featurefeeds.FeatureFeedsMicroSDK
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroResult
import com.raweng.dfe.models.config.DFEConfigCallback
import com.raweng.dfe.models.config.DFEConfigModel
import com.raweng.dfe.modules.policy.ErrorModel
import com.raweng.dfe.modules.policy.RequestType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.textV).setOnClickListener {
            fetchConfigAndInitMicroSDK()
            //fetchQuery()
        }

        //fetchDFEFeeds()
    }

    private fun fetchConfigAndInitMicroSDK() {
        var csApiKey = ""
        var csAccessToken = ""
        var csEnv = ""

        DFEManager.getInst().queryManager.getConfig(
            "",
            RequestType.Network,
            object : DFEConfigCallback() {
                override fun onCompletion(apiData: MutableList<DFEConfigModel>?, p1: ErrorModel?) {
                    if (!apiData.isNullOrEmpty()) {
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
                            /*if (contentStack.has("url")) {
                                configSkdModel.cmsUrl = contentStack.getString("url")
                            }*/
                        }
                        FeatureFeedsMicroSDK.init(
                            this@MainActivity,
                            "",
                            "",
                            csApiKey = csApiKey,
                            csAccessToken = csAccessToken,
                            csEnv = csEnv
                        )
                        fetchFeatureFeed()
                    } else {
                        Log.e("TAG", "onCompletion: " + p1.toString())
                    }
                }
            })
    }

    private fun fetchFeatureFeed() {
        FeatureFeedsMicroSDK.Companion.getInstance()
        val disposable =  FeatureFeedsMicroSDK
            .getInstance()
            .getFeatureFeed("featured_feeds")
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({
                when(it) {
                    is MicroResult.Failure -> {
                        Log.e("TAG", "fetchFeatureFeed: Failure", )
                    }
                    is MicroResult.Success -> {
                        it.data.map {
                            Log.e("TAG", "fetchFeatureFeed: Success"+it.title, )
                            it.feeds?.map {data->
                                Log.e("TAG", "fetchFeatureFeed: Success"+data.toString(), )
                            }
                        }
                    }
                }
            }) {
                Log.e("TAG", "fetchFeatureFeed: Error", )
            }
    }

    private fun fetchQuery() {

    }
}