package com.raweng.dfe.microsdk.featurefeeds.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.contentstack.sdk.*
import com.google.gson.Gson
import com.raweng.dfe.microsdk.featurefeeds.FeatureFeedsMicroSDK
import com.raweng.dfe.microsdk.featurefeeds.mapper.FeaturedFeedsMapper
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.utils.Utils
import com.raweng.dfe.models.feed.DFEFeedModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.Mockito.verify
import kotlin.coroutines.Continuation
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FeatureFeedRepositoryImplTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_fetchCMSFeedsResponse() = testScope.runTest {
        val repositoryMock = mockk<FeatureFeedRepositoryImpl>()
        val queryResultMock = mockk<QueryResult>()
        val errorMock = mockk<Error>()
        val cmsFeedsResponseFlow: Flow<QueryResult?> = flowOf(queryResultMock)
        val cmsFeedsNullResponseFlow: Flow<QueryResult?> = flowOf(null)

        val fetchCMSFeeds =
            FeatureFeedRepositoryImpl::class.memberFunctions.find { it.name == "fetchCMSFeeds" }
        fetchCMSFeeds?.isAccessible = true


        coEvery {
            fetchCMSFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<QueryResult?>>>()
            )
        } returns cmsFeedsResponseFlow

        val actualSuccessResult = runBlocking {
            fetchCMSFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<QueryResult?>>>()
            )
        }
        assertEquals(cmsFeedsResponseFlow, actualSuccessResult)


        coEvery {
            fetchCMSFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<QueryResult?>>>()
            )
        } returns cmsFeedsNullResponseFlow

        val actualSuccessNullResult = runBlocking {
            fetchCMSFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<QueryResult?>>>()
            )
        }

        assertEquals(cmsFeedsNullResponseFlow, actualSuccessNullResult)

        coEvery {
            fetchCMSFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<QueryResult?>>>()
            )
        } returns errorMock

        val actualErrorResult = runBlocking {
            fetchCMSFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<QueryResult?>>>()
            )
        }

        assertEquals(errorMock, actualErrorResult)

        testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun test_fetchDFEFeedsResponse() = testScope.runTest {
        val repositoryMock = mockk<FeatureFeedRepositoryImpl>()
        val feedResultMock = DFEFeedModel()
        val dfeFeedResponseFlow: Flow<DFEFeedModel?> = flow {
            emit(feedResultMock)
        }.catch {
            it.printStackTrace()
        }
        val dfeFeedNullResponseFlow: Flow<DFEFeedModel?> = flowOf(null)

        val fetchDFEFeeds =
            FeatureFeedRepositoryImpl::class.memberFunctions.find { it.name == "fetchDFEFeeds" }
        fetchDFEFeeds?.isAccessible = true


        coEvery {
            fetchDFEFeeds?.call(
                repositoryMock,
                anyString()
            )
        } returns dfeFeedResponseFlow

        val actualSuccessResult = runBlocking {
            fetchDFEFeeds?.call(
                repositoryMock,
                anyString()
            )
        }
        assertEquals(dfeFeedResponseFlow, actualSuccessResult)


        coEvery {
            fetchDFEFeeds?.call(
                repositoryMock,
                anyString()
            )
        } returns dfeFeedNullResponseFlow

        val actualSuccessNullResult = runBlocking {
            fetchDFEFeeds?.call(
                repositoryMock,
                anyString()
            )
        }

        assertEquals(dfeFeedNullResponseFlow, actualSuccessNullResult)
        coEvery {
            fetchDFEFeeds?.call(
                repositoryMock,
                anyString(),
            )
        } throws Throwable("my error")

        try {
            (fetchDFEFeeds?.call(repositoryMock, anyString()) as Flow<DFEFeedModel?>)
        } catch (e: Exception) {
            val result = e.cause?.message
            assertEquals("my error", result)
        }
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun updateFeedPositionShouldUpdatePositionCorrectly() {
        val position = 10
        val webUrlPosition = 20
        val feedType = mockk<FeatureFeedResponse.Entry.FeedType>()
        val article = FeatureFeedResponse.Entry.FeedType.Article(position = position)
        val gallery = FeatureFeedResponse.Entry.FeedType.Gallery(position = position)
        val video = FeatureFeedResponse.Entry.FeedType.Video(position = position)
        val webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl(position = webUrlPosition)
        val repositoryMock = mockk<FeatureFeedRepositoryImpl>()

        val updateFeedPosition =
            FeatureFeedRepositoryImpl::class.memberFunctions.find { it.name == "updateFeedPosition" }
        updateFeedPosition?.isAccessible = true

        every { updateFeedPosition?.call(repositoryMock, position, feedType) } returns {
            article
        }
        updateFeedPosition?.call(repositoryMock, position, feedType)
        assertEquals(article.position, position)


        every { updateFeedPosition?.call(repositoryMock, position, feedType) } returns {
            gallery
        }
        updateFeedPosition?.call(repositoryMock, position, feedType)
        assertEquals(gallery.position, position)

        every { updateFeedPosition?.call(repositoryMock, position, feedType) } returns {
            video
        }
        updateFeedPosition?.call(repositoryMock, position, feedType)
        assertEquals(video.position, position)

        every { updateFeedPosition?.call(repositoryMock, position, feedType) } returns {
            webUrl
        }
        updateFeedPosition?.call(repositoryMock, position, feedType)
        assertEquals(webUrl.position, webUrlPosition)
    }

    @Test
    fun test_fetchAndModifiedFeedTypeNBAData() = testScope.runTest {
        val mockStack = mockk<Stack>()
        val repositoryMock = FeatureFeedRepositoryImpl(mockStack)
        val articleFeed = FeatureFeedResponse.Entry.FeedType.Article().apply {
            position = 1
        }
        val feedType = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = null
            article = articleFeed
        }

        val feedTypeNba = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    value = "1052120"
                }
            }
            article = null
        }

        val fetchAndModifiedFeedTypeNBAData =
            FeatureFeedRepositoryImpl::class.memberFunctions.find { it.name == "fetchAndModifiedFeedTypeNBAData" }
        fetchAndModifiedFeedTypeNBAData?.isAccessible = true


        val result = fetchAndModifiedFeedTypeNBAData?.callSuspend(
            repositoryMock,
            2,
            feedType
        )
        assertEquals(feedType.apply { article?.position = 2 }, result)


        val resultNba = fetchAndModifiedFeedTypeNBAData?.callSuspend(
            repositoryMock,
            3,
            feedTypeNba
        )
        val expectedRes = feedTypeNba.apply { nbaFeeds?.nbaFeeds?.nbaFeedModel?.position = 3 }
        assertEquals(expectedRes, resultNba)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun test_getFeatureFeedResponseList() = testScope.runTest {

        FeatureFeedsMicroSDK.initialize(context,
        csAccessToken = null,
        csHostUrl = null,
        csApiKey = null,
        environment = null,
        imageFormat = null,
        dateFormat = null,
        dateFormatType = null,
        appScheme = null)
        val stack = mockk<Stack>()
        val entry = mockk<Entry>()
        val repositoryMock = FeatureFeedRepositoryImpl(stack)
        val getFeatureFeedResponseList =
            FeatureFeedRepositoryImpl::class.memberFunctions.find { it.name == "getFeatureFeedResponseList" }
        getFeatureFeedResponseList?.isAccessible = true
        val queryResult = mockk<QueryResult>()
        every { entry.toJSON() } returns JSONObject(getLocalJSONString())
        every { queryResult.resultObjects } returns listOf(entry)
        val result = getFeatureFeedResponseList?.callSuspend(
            repositoryMock,
            queryResult
        )
        assertEquals(getExpectList(), result)
        testDispatcher.scheduler.advanceUntilIdle()
    }

    private fun getLocalJSONString(): String {
        return "{\"_version\":6,\"locale\":\"en-us\",\"uid\":\"blt04d816c7bcb06e66\",\"ACL\":{},\"_in_progress\":false,\"created_at\":\"2023-06-07T10:40:38.369Z\",\"created_by\":\"bltbda632201ac50c92\",\"feed_type\":[{\"web_url\":{\"title\":\"Web\",\"_metadata\":{\"uid\":\"cs6ad53b1dd41b7730\"},\"hide_title\":false,\"published_date\":\"2023-06-07T10:36:54.000Z\",\"hide_date\":false,\"hide_label\":false,\"full_width_image\":null,\"half_width_image\":{\"uid\":\"blt804f1c84920338d8\",\"created_by\":\"blt34f1e9fe2a5e84b3\",\"updated_by\":\"blt34f1e9fe2a5e84b3\",\"created_at\":\"2022-06-29T10:29:10.133Z\",\"updated_at\":\"2022-06-29T10:29:10.133Z\",\"content_type\":\"image\\/png\",\"file_size\":\"71405\",\"filename\":\"Image3.png\",\"title\":\"Image3.png\",\"ACL\":{},\"_version\":1,\"parent_uid\":null,\"is_dir\":false,\"tags\":[],\"publish_details\":{\"environment\":\"blt6c85e28f0ce377e3\",\"locale\":\"en-us\",\"time\":\"2022-08-01T12:17:08.639Z\",\"user\":\"blt9d74b84f6fe5a0eb\"},\"url\":\"https:\\/\\/images.contentstack.io\\/v3\\/assets\\/blt8f37077c228fc690\\/blt804f1c84920338d8\\/62bc29769a899c5099f03b04\\/Image3.png\"},\"link\":\"https:\\/\\/www.google.com\"}},{\"web_url\":{\"title\":\"Image\",\"_metadata\":{\"uid\":\"csfd64e1a4da6a934d\"},\"hide_title\":false,\"published_date\":\"2023-06-07T10:40:11.000Z\",\"hide_date\":false,\"hide_label\":false,\"full_width_image\":null,\"half_width_image\":{\"uid\":\"blt710df69ab9d0b345\",\"created_by\":\"blt34f1e9fe2a5e84b3\",\"updated_by\":\"blt34f1e9fe2a5e84b3\",\"created_at\":\"2022-06-29T10:32:04.850Z\",\"updated_at\":\"2022-06-29T10:32:04.850Z\",\"content_type\":\"image\\/png\",\"file_size\":\"24915\",\"filename\":\"Image4.png\",\"title\":\"Image4.png\",\"ACL\":{},\"_version\":1,\"parent_uid\":null,\"is_dir\":false,\"tags\":[],\"publish_details\":{\"environment\":\"blt6c85e28f0ce377e3\",\"locale\":\"en-us\",\"time\":\"2022-06-29T10:32:16.292Z\",\"user\":\"blt34f1e9fe2a5e84b3\"},\"url\":\"https:\\/\\/images.contentstack.io\\/v3\\/assets\\/blt8f37077c228fc690\\/blt710df69ab9d0b345\\/62bc2a2467c84b506fe7955b\\/Image4.png\"},\"link\":\"www.google.com\"}}],\"order\":8,\"tags\":[],\"title\":\"Checking half width image\",\"updated_at\":\"2023-06-08T10:49:58.616Z\",\"updated_by\":\"bltbda632201ac50c92\",\"publish_details\":{\"environment\":\"blt6c85e28f0ce377e3\",\"locale\":\"en-us\",\"time\":\"2023-06-08T10:50:05.280Z\",\"user\":\"bltbda632201ac50c92\"}}"
    }

    private fun getExpectList():List<FeaturedFeedModel> {
        return listOf(
            FeaturedFeedModel(
                title = "Checking half width image",
                isoDate = "2023-06-08T10:49:58.616Z",
                updatedAt = "June 08, 2023", uid = "blt04d816c7bcb06e66",
                order = 8,
                feeds = listOf(
                    FeaturedFeedModel.FeedModel(
                        title = "Web",
                        date = "June 07, 2023",
                        thumbnail = "https://images.contentstack.io/v3/assets/blt8f37077c228fc690/blt804f1c84920338d8/62bc29769a899c5099f03b04/Image3.png",
                        feedType = "web_url",
                        feedId = "cs6ad53b1dd41b7730",
                        feedPosition = 0,
                        label = "WEB_URL",
                        hideTitle = false,
                        hideDate = false, hideLabel = false, dataSource = "CONTENTSTACK",
                        clickthroughLink = "null://screen/feed_detail/web_url?position=0&source=contentstack&feed_id=cs6ad53b1dd41b7730&uid=blt04d816c7bcb06e66",
                        link = "https://www.google.com",
                        content = "",
                        additionalContent = "",
                        galleryImages = listOf(), spotlightImage = "", author = null
                    ),
                    FeaturedFeedModel.FeedModel(
                        title = "Image",
                        date = "June 07, 2023",
                        thumbnail = "https://images.contentstack.io/v3/assets/blt8f37077c228fc690/blt710df69ab9d0b345/62bc2a2467c84b506fe7955b/Image4.png",
                        feedType = "web_url",
                        feedId = "csfd64e1a4da6a934d",
                        feedPosition = 1,
                        label = "WEB_URL",
                        hideTitle = false,
                        hideDate = false,
                        hideLabel = false,
                        dataSource = "CONTENTSTACK",
                        clickthroughLink = "null://screen/feed_detail/web_url?position=1&source=contentstack&feed_id=csfd64e1a4da6a934d&uid=blt04d816c7bcb06e66",
                        link = "www.google.com",
                        content = "",
                        additionalContent = "",
                        galleryImages = listOf(),
                        spotlightImage = "",
                        author = null
                    )
                ),
            )
        )
    }
}
