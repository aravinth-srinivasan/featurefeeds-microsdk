package com.raweng.microsdkapplication.featurefeed

import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe_components_android.components.featuredfeeds.model.FeaturedFeed

class FeaturedFeedDataMapper(private val featuredFeeds: List<FeaturedFeedModel>) {

    fun getFeaturedFeedList(): MutableList<MutableList<FeaturedFeed?>?> {
      return featuredFeeds.map {
            val mFeedList = it.feeds?.map { feed ->
               val feed:FeaturedFeed? = FeaturedFeed().apply {
                    uid = feed.clickthroughLink
                    imageUrl = feed.thumbnail
                    imageTag = if (feed.hideLabel == true) "" else feed.label
                    title = feed.title
                    subtitle = feed.date
                    imageAltText = ""
                }
                feed
            }?.toMutableList() ?: mutableListOf()
            mFeedList
        }.toMutableList()
    }
}