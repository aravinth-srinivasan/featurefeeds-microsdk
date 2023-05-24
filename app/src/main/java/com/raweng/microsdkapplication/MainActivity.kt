package com.raweng.microsdkapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.raweng.dfe.DFEManager
import com.raweng.dfe.microsdk.featurefeeds.FeatureFeedsMicroSDK
import com.raweng.dfe.microsdk.featurefeeds.model.ConfigModel
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
        fetchConfigAndInitMicroSDK()
        //fetchDFEFeeds()
    }

    private fun fetchConfigAndInitMicroSDK() {
        DFEManager.getInst().queryManager.getConfig(
            "",
            RequestType.Network,
            object : DFEConfigCallback() {
                override fun onCompletion(apiData: MutableList<DFEConfigModel>?, p1: ErrorModel?) {
                    if (!apiData.isNullOrEmpty()) {
                        val configSkdModel = ConfigModel()
                        val mConfigModel = apiData[0]
                        val data = JSONObject(mConfigModel.integrations)
                        if (data.has("contentstack")) {
                            val contentStack = data.getJSONObject("contentstack")
                            if (contentStack.has("app_key")) {
                                configSkdModel.cmsApiKey = contentStack.getString("app_key")
                            }

                            if (contentStack.has("environment")) {
                                configSkdModel.environment = contentStack.getString("environment")
                            }

                            /*if (contentStack.has("access_token")) {
                            //configSkdModel.cmsAccessToken = contentStack.getString("access_token")
                        }*/
                            if (contentStack.has("delivery_token")) {
                                configSkdModel.cmsAccessToken =
                                    contentStack.getString("delivery_token")
                            }
                            if (contentStack.has("url")) {
                                configSkdModel.cmsUrl = contentStack.getString("url")
                            }
                        }
                        FeatureFeedsMicroSDK.init(this@MainActivity, configSkdModel)
                        fetchFeatureFeed()
                    } else {
                        Log.e("TAG", "onCompletion: " + p1.toString())
                    }
                }
            })
    }

    private fun fetchFeatureFeed() {
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
                        Log.e("TAG", "fetchFeatureFeed: Success"+it.data.toString(), )
                    }
                }
            }) {
                Log.e("TAG", "fetchFeatureFeed: Error", )
            }
    }
}