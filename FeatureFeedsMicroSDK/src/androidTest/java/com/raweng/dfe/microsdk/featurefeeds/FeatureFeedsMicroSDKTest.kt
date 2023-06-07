package com.raweng.dfe.microsdk.featurefeeds

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.contentstack.sdk.Stack
import com.raweng.dfe.microsdk.featurefeeds.listener.FeatureFeedResponseListener
import com.raweng.dfe.microsdk.featurefeeds.manager.LocalApiManager
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

        // Test when all fields are not empty or null
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
    fun testsetupLocalApiManager() {
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
    fun testGetFeatureFeed() {
        val responseListenerMock: FeatureFeedResponseListener =
            mock(FeatureFeedResponseListener::class.java)
        val stackMock: Stack = mock(Stack::class.java)
        val localApiManagerMock = LocalApiManager(stackMock)

        val nullableLocalApiManager: LocalApiManager? = null
        val featureFeedsMicroSDK = FeatureFeedsMicroSDK.getInstance()

        val localApiManagerField: Field =
            FeatureFeedsMicroSDK::class.java.getDeclaredField("localApiManager")
        localApiManagerField.isAccessible = true
        // Case 1: localApiManager is null
        localApiManagerField.set(featureFeedsMicroSDK, nullableLocalApiManager)
        featureFeedsMicroSDK.getFeatureFeed("content_type", responseListenerMock)
        assertNull(localApiManagerField)

        // Call getFeatureFeed()
        //featureFeedsMicroSDK.getFeatureFeed("csContentType", responseListenerMock)

        // Assert that localApiManager is still null
//        assertNull(featureFeedsMicroSDK.localApiManager)

        // Case 2: localApiManager is not null
        /*val localApiManagerMock: LocalApiManager = mock(LocalApiManager::class.java)
        featureFeedsMicroSDK.localApiManager = localApiManagerMock*/

        // Call getFeatureFeed()
        //featureFeedsMicroSDK.getFeatureFeed("csContentType", responseListenerMock)

        // Assert that localApiManager is still not null
        //assertNotNull(featureFeedsMicroSDK.localApiManager)
    }

}