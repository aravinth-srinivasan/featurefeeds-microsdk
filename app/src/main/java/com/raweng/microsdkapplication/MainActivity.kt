package com.raweng.microsdkapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.gson.Gson
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
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneOffset

class MainActivity : AppCompatActivity() {

    var csApiKey = ""
    var csAccessToken = ""
    var csEnv = ""
    var csUrl = ""
    val builder = StringBuilder()
    var featureFeeds = listOf<FeaturedFeedModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchConfigAndInitMicroSDK()
        findViewById<AppCompatButton>(R.id.btnFetch).setOnClickListener {
            fetchFeatureFeed()
        }
        findViewById<AppCompatButton>(R.id.btnFetchFeed).setOnClickListener {
            getFeedModel()
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
                        FeatureFeedsMicroSDK.initialize(
                            this@MainActivity,
                            csHostUrl = csUrl,
                            csApiKey = csApiKey,
                            csAccessToken = csAccessToken,
                            environment = csEnv,
                            appScheme = "bulls",
                            dateFormatType = DateFormat.HOURS_AGO,
                            imageFormat = "format=pjpg&auto=webp",
                            dateFormat = "dd/MM/yyyy hh:mm a"
                        )
                    } else {
                        Log.e("TAG", "onCompletion: " + p1.toString())
                    }

                    Log.e(
                        "TAG",
                        "onCompletion: csUrl: $csUrl" +
                                " csApiKey: $csApiKey" +
                                " csAccessToken: $csAccessToken" +
                                "csEnv: $csEnv",
                    )
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
                    val gson  = Gson()
                    val finalOutput = gson.toJson(feeds)
                    Log.e("TAG", "onSuccess: $finalOutput")
                    featureFeeds = feeds
                    feeds.forEach {
                        onPrepareBuilder(it)
                    }
                    findViewById<TextView>(R.id.textV).text = builder.toString()
                }

                override fun onError(error: MicroError) {
                    Log.e("TAG", "onError: " + error.errorMsg)
                    findViewById<TextView>(R.id.textV).text = error.errorMsg
                }
            })
    }
    private fun getFeedModel(){
        builder.clear()
        findViewById<TextView>(R.id.textV).text = "Please wait querying..."
        try {
            val uid  = featureFeeds[0].uid
            val feed = FeatureFeedsMicroSDK.getInstance().getFeaturedFeedsModel(uid)
            feed?.let {
                onPrepareBuilder(feed)
            }
            findViewById<TextView>(R.id.textV).text = builder.toString()
        }catch (e:Exception) {
            e.printStackTrace()
        }
    }

    private fun onPrepareBuilder(it:FeaturedFeedModel){
        builder.append("Title : ${it.title}")
        builder.append("\n")
        builder.append("Order: ${it.order}")
        builder.append("\n")
        builder.append("UpdatedAt : ${it.updatedAt}")
        builder.append("\n")
        builder.append("Uid: ${it.uid}")
        builder.append("\n")
        //onPrepareFeedItem(it)
    }

    private fun onPrepareFeedItem(it:FeaturedFeedModel){
        it.feeds?.forEach {
            builder.append("\n")
            builder.append("\n")
            builder.append("Feed Title: ${it.title}")
            builder.append("\n")
            builder.append("Feed : Date: ${it.date}")
            builder.append("\n")
            builder.append("Feed : Thumbnail: ${it.thumbnail}")
            Log.e("TAG", "onSuccess: " + it.thumbnail)
            builder.append("\n")
            builder.append("Feed : Feed Type: ${it.feedType}")
            builder.append("\n")
            builder.append("Feed : Feed Id: ${it.feedId}")
            builder.append("\n")
            builder.append("Feed : Feed Position: ${it.feedPosition}")
            builder.append("\n")
            builder.append("Feed : Feed Label: ${it.label}")
            builder.append("\n")
            builder.append("Feed : Feed Hide Title: ${it.hideTitle}")
            builder.append("\n")
            builder.append("Feed : Feed Hide Date: ${it.hideDate}")
            builder.append("\n")
            builder.append("Feed : Feed Hide Label: ${it.hideLabel}")
            builder.append("\n")
            builder.append("Feed : Feed Hide Data Source: ${it.dataSource}")
            builder.append("\n")
            builder.append("Feed : ClickthroughLink: ${it.clickthroughLink}")
            builder.append("\n")
            builder.append("Feed : Link: ${it.link}")
            builder.append("\n")
            builder.append("Feed : Content: ${it.content?.take(100)}")
            builder.append("\n")
            builder.append("Feed : Additional Content: ${it.additionalContent?.take(100)}")
            builder.append("\n")
            builder.append("\n")
            builder.append("\n")
            if (!it.galleryImages.isNullOrEmpty()) {
                builder.append("Feed : Gallery Images: ")
                builder.append("\n")
                builder.append("\n")
                it.galleryImages?.mapIndexed { index, galleryModel ->
                    builder.append("${(index+1)} Gallery Title : ${it.title}")
                    builder.append("\n")
                    builder.append("${(index+1)} Gallery Url : ${galleryModel.url}")
                    builder.append("\n")
                    builder.append("${(index+1)} Gallery Caption : ${galleryModel.caption}")
                    builder.append("\n")
                    builder.append("${(index+1)} Gallery Type : ${galleryModel.imageType}")
                    builder.append("\n")
                    builder.append("\n")
                }
            }

            if (it.author !=null) {
                builder.append("Feed : Author: ")
                builder.append("\n")
                builder.append("\n")
                builder.append("Feed : Author: Name: ${it.author?.authorName}")
                builder.append("\n")
                builder.append("Feed : Author: Organization: ${it.author?.organization}")
                builder.append("\n")
                builder.append("Feed : Author: Image: ${it.author?.authorImage}")
                builder.append("\n")
                builder.append("Feed : Author: isFeaturedAuthor: ${it.author?.isFeaturedAuthor}")
                builder.append("\n")
                builder.append("Feed : Author: Description: ${it.author?.description}")
                builder.append("\n")
                builder.append("Feed : Author: isNbaStaff: ${it.author?.isNbaStaff}")
                builder.append("\n")
                builder.append("\n")
            }
            Log.e("TAG", "onSuccess: ${it.feedType} "+it.date)
            builder.append("\n")
            builder.append("\n")
        }
    }
}