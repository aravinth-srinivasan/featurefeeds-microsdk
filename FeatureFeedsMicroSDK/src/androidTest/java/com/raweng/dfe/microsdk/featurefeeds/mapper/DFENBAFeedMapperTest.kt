package com.raweng.dfe.microsdk.featurefeeds.mapper

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raweng.dfe.microsdk.featurefeeds.model.AuthorModel
import com.raweng.dfe.microsdk.featurefeeds.model.NbaFeedModel
import com.raweng.dfe.models.feed.DFEFeedModel
import com.raweng.dfe.models.feed.FeedMedia
import com.raweng.dfe.models.feed.FeedVideo
import io.realm.RealmList
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

@RunWith(AndroidJUnit4::class)
class DFENBAFeedMapperTest {

    @Test
    fun test_getMedia_validate_positive_and_negative() {
        val feedPos = 1
        val model = FeedMedia().apply {
            thumbnail = "https://test.com/test.jpg"
            source = "https://test.s/new.jpg"
            caption = "Caption Test"
            type = "Image"
            portrait = "test"
        }
        val dfeMock = DFEFeedModel().apply {
            media = RealmList<FeedMedia>().apply {
                add(model)
            }
        }

        val getMedia = DFENBAFeedMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getMedia" }
            ?: throw AssertionError("Private method not found")
        getMedia.isAccessible = true


        val expectedModel = NbaFeedModel.FeedMedia(
            thumbnail = model.thumbnail,
            source = model.source,
            caption = model.caption,
            type = model.type,
            portrait = model.portrait,
            title = null
        )

        val mapperMock = DFENBAFeedMapper(feedPos, dfeMock)
        val result = getMedia.call(mapperMock)
        assertEquals(listOf(expectedModel), result)

        val dfeMock1 = DFEFeedModel().apply {
            media = null
        }

        val mapperMock1 = DFENBAFeedMapper(feedPos, dfeMock1)
        val result1 = getMedia.call(mapperMock1)
        assertEquals(null, result1)
    }

    @Test
    fun test_getVideo_validate_positive_and_negative() {
        val feedPos = 1
        val model = FeedVideo().apply {
            url = "https://test.com/test.mp4"
        }
        val dfeMock = DFEFeedModel().apply {
            video = RealmList<FeedVideo>().apply {
                add(model)
            }
        }

        val getVideo = DFENBAFeedMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getVideo" }
            ?: throw AssertionError("Private method not found")
        getVideo.isAccessible = true


        val expectedModel = NbaFeedModel.Video(
            url = model.url
        )

        val mapperMock = DFENBAFeedMapper(feedPos, dfeMock)
        val result = getVideo.call(mapperMock)
        assertEquals(listOf(expectedModel), result)

        val dfeMock1 = DFEFeedModel().apply {
            video = null
        }

        val mapperMock1 = DFENBAFeedMapper(feedPos, dfeMock1)
        val result1 = getVideo.call(mapperMock1)
        assertEquals(null, result1)
    }

    @Test
    fun test_getNbaFeedModel_validate_positive_and_negative() {
        val feedPos = 1
        val model = FeedVideo().apply {
            url = "https://test.com/test.mp4"
        }
        val dfeMock1 = DFEFeedModel().apply {
            uid = "12123409"
            newsid = "1020040"
            title = "Test title"
            publishedDate = "2023-04-21T16:57:31.000Z"
            video = RealmList<FeedVideo>().apply {
                add(model)
            }
            feedType = "article"
            content = "dfe content"
            additionalContent = "dfe additional content"
        }

        val expectedR1 = NbaFeedModel(
            uid = dfeMock1.uid,
            nid = dfeMock1.newsid,
            title = dfeMock1.title,
            publishedDateString = dfeMock1.publishedDate,
            publishedDate = null,
            feedType = dfeMock1.feedType,
            category = null,
            media = listOf(),
            position = feedPos,
            content = dfeMock1.content,
            additionalContent = dfeMock1.additionalContent,
            webUrl = "",
            video = listOf(NbaFeedModel.Video(url = model.url)),
            author = AuthorModel(
                authorName = "",
                organization = "",
                authorImage = "",
                isNbaStaff = false,
                description = "",
                isFeaturedAuthor = false
            )
        )

        val mapperMock1 = DFENBAFeedMapper(feedPos, dfeMock1)
        val result = mapperMock1.getNbaFeedModel()
        assertEquals(expectedR1, result)

        val mapperMock2 = DFENBAFeedMapper(feedPos, DFEFeedModel())
        val result2 = mapperMock2.getNbaFeedModel()
        val expectedR2 = NbaFeedModel().apply {
            uid = ""
            nid = ""
            title = ""
            publishedDateString = ""
            feedType = ""
            content = ""
            additionalContent = ""
            webUrl = ""
            video = listOf()
            media = listOf()
            position = feedPos
            author = AuthorModel().apply {
                authorImage = ""
                authorName = ""
                organization = ""
                isNbaStaff = false
                description = ""
                isFeaturedAuthor = false
            }
        }
        assertEquals(expectedR2, result2)
    }

}