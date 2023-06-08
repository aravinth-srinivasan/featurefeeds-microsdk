package com.raweng.dfe.microsdk.featurefeeds

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.contentstack.sdk.Stack
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.manager.LocalApiManager
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.type.DateFormat
import com.raweng.dfe.microsdk.featurefeeds.utils.MicroError
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import java.lang.reflect.Field
import java.lang.reflect.Method

@RunWith(AndroidJUnit4::class)
class FeatureFeedsMicroSDKTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testisAllFieldsAreNotEmptyOrNull() {
        val companion: Any =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("Companion").get(null) as Any
        val method: Method = companion.javaClass.getDeclaredMethod("isAllFieldsAreNotEmptyOrNull")
        method.isAccessible = true

        val csHostUrlField: Field = FeatureFeedsMicroSDK::class.java.getDeclaredField("csHostUrl")
        csHostUrlField.isAccessible = true

        val csApiKeyField: Field = FeatureFeedsMicroSDK::class.java.getDeclaredField("csApiKey")
        csApiKeyField.isAccessible = true

        val environmentField: Field =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("environment")
        environmentField.isAccessible = true

        val csAccessTokenField: Field =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("csAccessToken")
        csAccessTokenField.isAccessible = true

        csHostUrlField.set(null, "https://test.com")
        csApiKeyField.set(null, "api_key")
        environmentField.set(null, "environment")
        csAccessTokenField.set(null, "access_token")
        assertTrue(method.invoke(companion) as Boolean)


        csHostUrlField.set(null, null)
        csApiKeyField.set(null, null)
        environmentField.set(null, null)
        csAccessTokenField.set(null, null)
        assertFalse(method.invoke(companion) as Boolean)


        csHostUrlField.set(null, "https://example.com")
        csApiKeyField.set(null, null)
        environmentField.set(null, "environment")
        csAccessTokenField.set(null, "access_token")
        assertFalse(method.invoke(companion) as Boolean)
    }


    @Test
    fun test_setupLocalApiManager() {
        // Mock dependencies
        val stackMock: Stack = mock(Stack::class.java)
        val nullableStack: Stack? = null
        val localApiManagerMock = LocalApiManager(stackMock)
        val nullableLocalApiManager: LocalApiManager? = null
        val featureFeedsMicroSDK = FeatureFeedsMicroSDK.getInstance()

        val stackField: Field = FeatureFeedsMicroSDK::class.java.getDeclaredField("stack")
        stackField.isAccessible = true
        stackField.set(featureFeedsMicroSDK, stackMock)

        val localApiManagerField: Field =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("localApiManager")
        localApiManagerField.isAccessible = true
        localApiManagerField.set(featureFeedsMicroSDK, localApiManagerMock)

        val companion: Any =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("Companion").get(null) as Any
        val method: Method = companion.javaClass.getDeclaredMethod("setupLocalApiManager")
        method.isAccessible = true
        method.invoke(companion)
        assertNotNull(stackField)
        assertNotNull(localApiManagerField)

        stackField.set(featureFeedsMicroSDK, nullableStack)
        localApiManagerField.set(featureFeedsMicroSDK, nullableLocalApiManager)
        method.invoke(companion)
        assertNull(nullableStack)
        assertNull(nullableLocalApiManager)
    }

    @Test
    fun test_getFeaturedFeedsModel() {
        val localApiManager: LocalApiManager = mockk()
        val featureFeedsMicroSDK = spyk<FeatureFeedsMicroSDK>()
        val model = mockk<FeaturedFeedModel>()

        val localApiManagerField: Field =
            featureFeedsMicroSDK::class.java.getDeclaredField("localApiManager")
        localApiManagerField.isAccessible = true
        localApiManagerField.set(featureFeedsMicroSDK, localApiManager)

        val uid = "102033"
        every { featureFeedsMicroSDK.getFeaturedFeedsModel(uid) } returns model
        val result = featureFeedsMicroSDK.getFeaturedFeedsModel(uid)
        assertEquals(model, result)

        every { localApiManager.getFeaturedFeedsModel("") } returns null

        val resultIfEmpty = featureFeedsMicroSDK.getFeaturedFeedsModel("")
        assertEquals(null, resultIfEmpty)

    }

    @Test
    fun test_getFeatureFeed() {
        val responseListenerMock: FeatureFeedResponseListener =
            object : FeatureFeedResponseListener {
                override fun onSuccess(feeds: List<FeaturedFeedModel>) {
                    assertEquals(listOf(FeaturedFeedModel()), feeds)
                }

                override fun onError(error: MicroError) {
                    Log.d("TAG", "onError: " + error.errorMsg)
                    assertEquals("test error", error.errorMsg)
                }
            }

        val nullableLocalApiManager: LocalApiManager? = null
        val featureFeedsMicroSDK = spyk<FeatureFeedsMicroSDK>()

        val localApiManagerField: Field =
            featureFeedsMicroSDK::class.java.getDeclaredField("localApiManager")
        localApiManagerField.isAccessible = true
        localApiManagerField.set(featureFeedsMicroSDK, nullableLocalApiManager)

        val csContentType = "exampleContentType"
        featureFeedsMicroSDK.getFeatureFeed(csContentType, responseListenerMock)
        assertNull(nullableLocalApiManager)



        every {
            featureFeedsMicroSDK.getFeatureFeed(
                csContentType,
                any()
            )
        } answers {
            val listener = secondArg<FeatureFeedResponseListener>()
            listener.onSuccess(listOf(FeaturedFeedModel()))
        }
        featureFeedsMicroSDK.getFeatureFeed(csContentType, responseListenerMock)

        every {
            featureFeedsMicroSDK.getFeatureFeed(
                csContentType,
                any()
            )
        } answers {
            val listener = secondArg<FeatureFeedResponseListener>()
            listener.onError(MicroError(errorMsg = "test error"))
        }
    }


    @Test
    fun test_initialize() {

        val csHostUrlField: Field = FeatureFeedsMicroSDK::class.java.getDeclaredField("csHostUrl")
        csHostUrlField.isAccessible = true

        val csApiKeyField: Field = FeatureFeedsMicroSDK::class.java.getDeclaredField("csApiKey")
        csApiKeyField.isAccessible = true

        val environmentField: Field =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("environment")
        environmentField.isAccessible = true

        val csAccessTokenField: Field =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("csAccessToken")
        csAccessTokenField.isAccessible = true

        val appSchemeField: Field =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("appScheme")
        appSchemeField.isAccessible = true

        val dateFormatTypeField: Field =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("dateFormatType")
        dateFormatTypeField.isAccessible = true

        val dateFormatField: Field =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("dateFormat")
        dateFormatField.isAccessible = true


        val imageFormatField: Field =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("imageFormat")
        imageFormatField.isAccessible = true

        val expectedCSApiKey = "api121133455"
        val expectedCSHostUrl = "cnd.url-test.com"
        val expectedEnvironment = "dev"
        val expectedCSAccessToken = "121324der44433"
        val expectedAppScheme = "bulls"
        val expectedDateFormatType = DateFormat.HOURS_AGO
        val expectedDateFormat = "dd/MM/yyyy"
        val expectedImageFormat = "auto?webp"

        FeatureFeedsMicroSDK.initialize(
            context,
            csApiKey = expectedCSApiKey,
            csHostUrl = expectedCSHostUrl,
            environment = expectedEnvironment,
            csAccessToken = expectedCSAccessToken,
            appScheme = expectedAppScheme,
            dateFormatType = expectedDateFormatType,
            dateFormat = expectedDateFormat,
            imageFormat = expectedImageFormat
        )

        assertEquals(expectedCSApiKey, csApiKeyField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedCSHostUrl, csHostUrlField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedEnvironment, environmentField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedCSAccessToken, csAccessTokenField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedAppScheme, appSchemeField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedDateFormatType, dateFormatTypeField.get(FeatureFeedsMicroSDK) as DateFormat)
        assertEquals(expectedDateFormat, dateFormatField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedImageFormat, imageFormatField.get(FeatureFeedsMicroSDK) as String)






        FeatureFeedsMicroSDK.initialize(
            context,
            csApiKey = expectedCSApiKey,
            csHostUrl = expectedCSHostUrl,
            environment = expectedEnvironment,
            csAccessToken = expectedCSAccessToken,
            appScheme = expectedAppScheme,
            dateFormatType = expectedDateFormatType,
            dateFormat = null,
            imageFormat = expectedImageFormat
        )

        assertEquals(expectedCSApiKey, csApiKeyField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedCSHostUrl, csHostUrlField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedEnvironment, environmentField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedCSAccessToken, csAccessTokenField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedAppScheme, appSchemeField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedDateFormatType, dateFormatTypeField.get(FeatureFeedsMicroSDK) as DateFormat)
        assertEquals("MMMM dd, yyyy", dateFormatField.get(FeatureFeedsMicroSDK) as String)
        assertEquals(expectedImageFormat, imageFormatField.get(FeatureFeedsMicroSDK) as String)

    }

}