package com.raweng.dfe.microsdk.featurefeeds.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.contentstack.sdk.*
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse
import com.raweng.dfe.models.feed.DFEFeedModel
import com.raweng.dfe.modules.policy.ErrorModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import kotlin.coroutines.Continuation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FeatureFeedRepositoryImplTest {
    val context = ApplicationProvider.getApplicationContext<Context>()

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
    fun testFetchCMSFeedsResponse() = testScope.runTest {
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
    fun testFetchDFEFeedsResponse() = testScope.runTest {
        val repositoryMock = mockk<FeatureFeedRepositoryImpl>()
        val feedResultMock = mockk<DFEFeedModel>() //TODO Need to report rajesh
        val errorMock = mockk<ErrorModel>()
        val dfeFeedResponseFlow: Flow<DFEFeedModel?> = flowOf(feedResultMock)
        val dfeFeedNullResponseFlow: Flow<DFEFeedModel?> = flowOf(null)

        val fetchDFEFeeds =
            FeatureFeedRepositoryImpl::class.memberFunctions.find { it.name == "fetchDFEFeeds" }
        fetchDFEFeeds?.isAccessible = true


        coEvery {
            fetchDFEFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<DFEFeedModel?>>>()
            )
        } returns dfeFeedResponseFlow

        val actualSuccessResult = runBlocking {
            fetchDFEFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<DFEFeedModel?>>>()
            )
        }
        assertEquals(dfeFeedResponseFlow, actualSuccessResult)


        coEvery {
            fetchDFEFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<DFEFeedModel?>>>()
            )
        } returns dfeFeedNullResponseFlow

        val actualSuccessNullResult = runBlocking {
            fetchDFEFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<DFEFeedModel?>>>()
            )
        }

        assertEquals(dfeFeedNullResponseFlow, actualSuccessNullResult)

        coEvery {
            fetchDFEFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<DFEFeedModel?>>>()
            )
        } returns errorMock

        val actualErrorResult = runBlocking {
            fetchDFEFeeds?.call(
                repositoryMock,
                anyString(),
                any<Continuation<Flow<DFEFeedModel?>>>()
            )
        }

        assertEquals(errorMock, actualErrorResult)

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


}
