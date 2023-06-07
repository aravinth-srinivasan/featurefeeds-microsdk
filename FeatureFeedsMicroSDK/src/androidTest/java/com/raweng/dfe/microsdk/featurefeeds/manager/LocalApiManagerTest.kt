package com.raweng.dfe.microsdk.featurefeeds.manager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroError
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class LocalApiManagerTest {

    private lateinit var localApiManager: LocalApiManager

    private val context = ApplicationProvider.getApplicationContext<Context>()



    @Test
    fun fetchFeatureFeed_withNonValidContentType_callsRepositoryMethod() {
        val csContentType = ""
        val expectedErrorModel = mockk<MicroError>()
        val localApiManagerMock = mockk<LocalApiManager>()
        val expectedSuccessResponse = mockk<List<FeaturedFeedModel>>()
        val responseListener = object : FeatureFeedResponseListener {
            override fun onSuccess(feeds: List<FeaturedFeedModel>) {
                assertEquals(expectedSuccessResponse, feeds)
            }

            override fun onError(error: MicroError) {
                assertEquals(expectedErrorModel, error)
            }
        }


        every { localApiManagerMock.fetchFeatureFeed(csContentType, responseListener) } answers {
            responseListener.onError(expectedErrorModel)
        }
        localApiManagerMock.fetchFeatureFeed(csContentType, responseListener)

        every { localApiManagerMock.fetchFeatureFeed(csContentType, responseListener) } answers {
            responseListener.onSuccess(expectedSuccessResponse)
        }
        localApiManagerMock.fetchFeatureFeed(csContentType, responseListener)
    }

    @Test
    fun testGetFeaturedFeedsModel_WithValidUid_ReturnsModel() {
        val uid = "123"
        val localApiManagerMock = mockk<LocalApiManager>()
        val expectedModel = FeaturedFeedModel()
        every { localApiManagerMock.getFeaturedFeedsModel(uid) } returns expectedModel
        val result = localApiManagerMock.getFeaturedFeedsModel(uid)
        assertEquals(expectedModel, result)

        every { localApiManagerMock.getFeaturedFeedsModel(uid) } returns null
        val resultNull = localApiManagerMock.getFeaturedFeedsModel(uid)
        assertEquals(null, resultNull)
    }
}

