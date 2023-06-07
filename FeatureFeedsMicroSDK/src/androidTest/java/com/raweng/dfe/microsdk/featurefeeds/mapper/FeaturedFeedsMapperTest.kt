package com.raweng.dfe.microsdk.featurefeeds.mapper

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raweng.dfe.microsdk.featurefeeds.FeatureFeedsMicroSDK
import com.raweng.dfe.microsdk.featurefeeds.data.FeatureFeedRepositoryImpl
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.model.NbaFeedModel
import com.raweng.dfe.microsdk.featurefeeds.type.DateFormat
import com.raweng.dfe.microsdk.featurefeeds.type.FeedType
import com.raweng.dfe.microsdk.featurefeeds.type.SourceType
import com.raweng.dfe.microsdk.featurefeeds.utils.Utils
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

@RunWith(AndroidJUnit4::class)
class FeaturedFeedsMapperTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val dateFormat = "MMMM dd, yyyy"

    @Test
    fun generateCMSImageUrlShouldReturnTheModifiedURL_forValidInput() {

        FeatureFeedsMicroSDK.initialize(
            context,
            dateFormatType = DateFormat.HOURS_AGO,
            csHostUrl = "w12121212",
            csAccessToken = "q20289129128912",
            csApiKey = "21212121212asa",
            dateFormat = dateFormat,
            environment = "dev",
            appScheme = "bulls"
        )

        val mockResponse = FeatureFeedResponse.Entry()
        val mMapperMock = FeaturedFeedsMapper(mockResponse)
        val url = "https://images.contentstack.io/Image-2.jpg"



        val generateCMSImageUrl = mMapperMock::class.declaredMemberFunctions
            .firstOrNull { it.name == "generateCMSImageUrl" }
            ?: throw AssertionError("Private method not found")
        generateCMSImageUrl.isAccessible = true

        val result = generateCMSImageUrl.call(mMapperMock, url) as String?
        assertEquals(url, result)

        FeatureFeedsMicroSDK.initialize(
            context,
            dateFormatType = DateFormat.HOURS_AGO,
            csHostUrl = "w12121212",
            csAccessToken = "q20289129128912",
            csApiKey = "21212121212asa",
            dateFormat = dateFormat,
            environment = "dev",
            appScheme = "bulls",
            imageFormat = "auto=webp"
        )

        val result1 = generateCMSImageUrl.call(mMapperMock, url) as String?
        assertEquals("$url?auto=webp", result1)

    }

    @Test
    fun test_getSourceWithValidInput() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)
        val feedTypeDFEMock = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    value = "102012"
                }
            }
        }

        val feedTypeCMS = FeatureFeedResponse.Entry.FeedType()

        val getSource = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getSource" }
            ?: throw AssertionError("Private method not found")
        getSource.isAccessible = true

        val expectedDFESource = SourceType.DFE.toString()
        val resultDFE = getSource.call(mapperMock, feedTypeDFEMock)
        assertEquals(expectedDFESource, resultDFE)

        val expectedCMSSource = SourceType.CONTENTSTACK.toString()
        val resultCMS = getSource.call(mapperMock, feedTypeCMS)
        assertEquals(expectedCMSSource, resultCMS)

        val expectedCMSSourceIfNullFeed = SourceType.CONTENTSTACK.toString()
        val resultIfNull = getSource.call(mapperMock, null)
        assertEquals(expectedCMSSourceIfNullFeed, resultIfNull)
    }

    @Test
    fun test_getFeedPosition_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)
        val expectedPosition = 10
        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                position = expectedPosition
            }
        }

        val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery().apply {
                position = expectedPosition
            }
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                position = expectedPosition
            }
        }

        val feedTypeWebUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl().apply {
                position = expectedPosition
            }
        }

        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel = NbaFeedModel().apply {
                        position = expectedPosition
                    }
                }
            }
        }

        val getFeedPosition = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getFeedPosition" }
            ?: throw AssertionError("Private method not found")
        getFeedPosition.isAccessible = true


        val resultArticle = getFeedPosition.call(mapperMock, feedTypeArticle)
        assertEquals(expectedPosition, resultArticle)

        val resultGallery = getFeedPosition.call(mapperMock, feedTypeGallery)
        assertEquals(expectedPosition, resultGallery)

        val resultVideo = getFeedPosition.call(mapperMock, feedTypeVideo)
        assertEquals(expectedPosition, resultVideo)

        val resultWebUrl = getFeedPosition.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedPosition, resultWebUrl)

        val resultNBA = getFeedPosition.call(mapperMock, feedTypeNBA)
        assertEquals(expectedPosition, resultNBA)

        val resultNull = getFeedPosition.call(mapperMock, null)
        assertEquals(null, resultNull)

    }


    @Test
    fun test_getFeedId_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val expectedUid = "101asnja383u11"
        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                metadata = FeatureFeedResponse.Metadata().apply {
                    uid = expectedUid
                }
            }
        }

        val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery().apply {
                metadata = FeatureFeedResponse.Metadata().apply {
                    uid = expectedUid
                }
            }
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                metadata = FeatureFeedResponse.Metadata().apply {
                    uid = expectedUid
                }
            }
        }

        val feedTypeWebUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl().apply {
                metadata = FeatureFeedResponse.Metadata().apply {
                    uid = expectedUid
                }
            }
        }

        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel =NbaFeedModel().apply {
                        nid = expectedUid
                    }
                }
            }
        }

        val getFeedId = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getFeedId" }
            ?: throw AssertionError("Private method not found")
        getFeedId.isAccessible = true

        val resultArticle = getFeedId.call(mapperMock, feedTypeArticle)
        assertEquals(expectedUid, resultArticle)

        val resultGallery = getFeedId.call(mapperMock, feedTypeGallery)
        assertEquals(expectedUid, resultGallery)

        val resultVideo = getFeedId.call(mapperMock, feedTypeVideo)
        assertEquals(expectedUid, resultVideo)

        val resultWebUrl = getFeedId.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedUid, resultWebUrl)

        val resultNBA = getFeedId.call(mapperMock, feedTypeNBA)
        assertEquals(expectedUid, resultNBA)

        val resultNull = getFeedId.call(mapperMock, null)
        assertEquals(null, resultNull)

    }

    @Test
    fun test_getGenerateClickThroughLink_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        FeatureFeedsMicroSDK.initialize(
            context,
            dateFormatType = DateFormat.HOURS_AGO,
            csHostUrl = "w12121212",
            csAccessToken = "q20289129128912",
            csApiKey = "21212121212asa",
            dateFormat = dateFormat,
            environment = "dev",
            appScheme = "bulls"
        )


        val feedType = FeaturedFeedModel.FeedModel().apply {
            feedType = FeedType.ARTICLE.toString()
            dataSource = SourceType.CONTENTSTACK.toString()
            feedId = "asjaj13186316371136"
            feedPosition = 1
        }

        val feedModel = FeaturedFeedModel().apply {
            uid = "aslaksakjsjahs"
        }

        val expectedResult = "bulls://screen/feed_detail/" +
                (feedType.feedType?.lowercase() ?: "") +
                "?position=${feedType.feedPosition ?: 0}" +
                "&source=${feedType.dataSource?.lowercase() ?: ""}" +
                "&feed_id=${feedType.feedId ?: ""}" +
                "&uid=${feedModel.uid}"

        val getGenerateClickThroughLink = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getGenerateClickThroughLink" }
            ?: throw AssertionError("Private method not found")
        getGenerateClickThroughLink.isAccessible = true
        val result = getGenerateClickThroughLink.call(mapperMock, feedModel, feedType)
        assertEquals(expectedResult, result)
    }

    @Test
    fun test_getHideLabel_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)
        val expectedHideLabel1 = true
        val expectedHideLabel2 = false
        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                hideLabel = expectedHideLabel1
            }
        }

        val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery().apply {
                hideLabel = expectedHideLabel2
            }
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                hideLabel = expectedHideLabel1
            }
        }

        val feedTypeWebUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl().apply {
                hideLabel = expectedHideLabel2
            }
        }

        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds()
                }
            }
        }

        val getHideLabel = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getHideLabel" }
            ?: throw AssertionError("Private method not found")
        getHideLabel.isAccessible = true

        val resultArticle = getHideLabel.call(mapperMock, feedTypeArticle)
        assertEquals(expectedHideLabel1, resultArticle)

        val resultGallery = getHideLabel.call(mapperMock, feedTypeGallery)
        assertEquals(expectedHideLabel2, resultGallery)

        val resultVideo = getHideLabel.call(mapperMock, feedTypeVideo)
        assertEquals(expectedHideLabel1, resultVideo)

        val resultWebUrl = getHideLabel.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedHideLabel2, resultWebUrl)

        val resultNBA = getHideLabel.call(mapperMock, feedTypeNBA)
        assertEquals(expectedHideLabel2, resultNBA)

        val resultNull = getHideLabel.call(mapperMock, null)
        assertEquals(null, resultNull)

    }


    @Test
    fun test_getHideTitle_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val expectedHideLabel1 = true
        val expectedHideLabel2 = false
        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                hideTitle = expectedHideLabel1
            }
        }

        val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery().apply {
                hideTitle = expectedHideLabel2
            }
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                hideTitle = expectedHideLabel1
            }
        }

        val feedTypeWebUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl().apply {
                hideTitle = expectedHideLabel2
            }
        }

        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds()
                }
            }
        }

        val getHideTitle = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getHideTitle" }
            ?: throw AssertionError("Private method not found")
        getHideTitle.isAccessible = true

        val resultArticle = getHideTitle.call(mapperMock, feedTypeArticle)
        assertEquals(expectedHideLabel1, resultArticle)

        val resultGallery = getHideTitle.call(mapperMock, feedTypeGallery)
        assertEquals(expectedHideLabel2, resultGallery)

        val resultVideo = getHideTitle.call(mapperMock, feedTypeVideo)
        assertEquals(expectedHideLabel1, resultVideo)

        val resultWebUrl = getHideTitle.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedHideLabel2, resultWebUrl)

        val resultNBA = getHideTitle.call(mapperMock, feedTypeNBA)
        assertEquals(expectedHideLabel2, resultNBA)

        val resultNull = getHideTitle.call(mapperMock, null)
        assertEquals(null, resultNull)

    }

    @Test
    fun test_getHideDate_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val expectedHideLabel1 = true
        val expectedHideLabel2 = false
        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                hideDate = expectedHideLabel1
            }
        }

        val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery().apply {
                hideDate = expectedHideLabel2
            }
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                hideDate = expectedHideLabel1
            }
        }

        val feedTypeWebUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl().apply {
                hideDate = expectedHideLabel2
            }
        }

        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds()
                }
            }
        }

        val getHideDate = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getHideDate" }
            ?: throw AssertionError("Private method not found")
        getHideDate.isAccessible = true


        val resultArticle = getHideDate.call(mapperMock, feedTypeArticle)
        assertEquals(expectedHideLabel1, resultArticle)

        val resultGallery = getHideDate.call(mapperMock, feedTypeGallery)
        assertEquals(expectedHideLabel2, resultGallery)

        val resultVideo = getHideDate.call(mapperMock, feedTypeVideo)
        assertEquals(expectedHideLabel1, resultVideo)

        val resultWebUrl = getHideDate.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedHideLabel2, resultWebUrl)

        val resultNBA = getHideDate.call(mapperMock, feedTypeNBA)
        assertEquals(expectedHideLabel2, resultNBA)

        val resultNull = getHideDate.call(mapperMock, null)
        assertEquals(null, resultNull)

    }

    @Test
    fun test_getDFEThumbnail_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val getDFEThumbnail = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getDFEThumbnail" }
            ?: throw AssertionError("Private method not found")
        getDFEThumbnail.isAccessible = true

        val halfImageExpected = FeatureFeedResponse.Image().apply {
            url = "https://test.url"
        }

        val fullImageExpected = FeatureFeedResponse.Image().apply {
            url = "https://test123.url"
        }

        val feedType = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel = NbaFeedModel().apply {
                        halfWidthImage = halfImageExpected
                    }
                }
            }
        }

        val feedTypeFull = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel = NbaFeedModel().apply {
                        halfWidthImage = halfImageExpected
                        fullWidthImage = fullImageExpected
                    }
                }
            }
        }

        val totalSize = 2


        val result = getDFEThumbnail.call(mapperMock, totalSize, feedType)
        assertEquals(halfImageExpected.url, result)


        val resultFullWidth = getDFEThumbnail.call(mapperMock, totalSize, feedTypeFull)
        assertEquals(halfImageExpected.url, resultFullWidth)


        val resultBothImages = getDFEThumbnail.call(mapperMock, 0, feedTypeFull)
        assertEquals(fullImageExpected.url, resultBothImages)
    }

    @Test
    fun test_getThumbnail_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val image1 = FeatureFeedResponse.Image().apply {
            url = "https://www.goog.com"
        }

        val image2 = FeatureFeedResponse.Image().apply {
            url = "https://www.fb.com"
        }

        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                fullWidthImage = image1
                halfWidthImage = null
            }
        }

        val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery().apply {
                fullWidthImage = image1
                halfWidthImage = image2
            }
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                fullWidthImage = null
                halfWidthImage = null
            }
        }

        val feedTypeWebUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl().apply {
                fullWidthImage = null
                halfWidthImage = image2
            }
        }

        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    fullWidthImage = image1
                    halfWidthImage = image2
                }
            }
        }

        val getThumbnail = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getThumbnail" }
            ?: throw AssertionError("Private method not found")
        getThumbnail.isAccessible = true


        val resultArticle = getThumbnail.call(mapperMock, 1, feedTypeArticle)
        assertEquals(image1.url, resultArticle)

        val resultGallery = getThumbnail.call(mapperMock, 2, feedTypeGallery)
        assertEquals(image2.url, resultGallery)


        val resultVideo = getThumbnail.call(mapperMock, 1, feedTypeVideo)
        assertEquals(null, resultVideo)


        val resultWebUrl = getThumbnail.call(mapperMock, 1, feedTypeWebUrl)
        assertEquals(null, resultWebUrl)

        val resultNBA = getThumbnail.call(mapperMock, 2, feedTypeNBA)
        assertEquals(image2.url, resultNBA)


        val resultNull = getThumbnail.call(mapperMock, 0, null)
        assertEquals(null, resultNull)

    }

    @Test
    fun test_getDate_validate_positive_and_negative() {

        FeatureFeedsMicroSDK.initialize(
            context,
            dateFormatType = DateFormat.HOURS_AGO,
            csHostUrl = "w12121212",
            csAccessToken = "q20289129128912",
            csApiKey = "21212121212asa",
            dateFormat = dateFormat,
            environment = "dev",
            appScheme = "bulls"
        )

        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                publishedDate = "2023-06-07T13:07:51.000Z"
            }
        }

        val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery().apply {
                metadata = FeatureFeedResponse.Metadata().apply {
                    publishedDate = "2022-09-15T15:13:11.000Z"
                }
            }
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                metadata = FeatureFeedResponse.Metadata().apply {
                    publishedDate = "2022-09-15T15:25:17.000Z"
                }
            }
        }

        val feedTypeWebUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl().apply {
                metadata = FeatureFeedResponse.Metadata().apply {
                    publishedDate =  "2022-09-25T14:47:16.000Z"
                }
            }
        }

        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                metadata = FeatureFeedResponse.Metadata().apply {
                    nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                        nbaFeedModel = NbaFeedModel().apply {
                            publishedDateString = "1678209163000"
                        }
                    }
                }
            }
        }

        val mockResponse = mockk<FeatureFeedResponse.Entry>()
        val mMapperMock = FeaturedFeedsMapper(mockResponse)

        val getDate = mMapperMock::class.declaredMemberFunctions
            .firstOrNull { it.name == "getDate" }
            ?: throw AssertionError("Private method not found")
        getDate.isAccessible = true

        val expectedDate = Utils.parseDate(feedTypeArticle.article?.publishedDate!!)
        val expectedArticleDate = Utils.timeAgoSinceAccessibility(expectedDate!!, dateFormat)
        val resultArticle = getDate.call(mMapperMock, feedTypeArticle)
        assertEquals(expectedArticleDate, resultArticle)


        val expectedGallery = Utils.parseDate(feedTypeGallery.gallery?.publishedDate!!)
        val expectedGalleryDate = Utils.timeAgoSinceAccessibility(expectedGallery!!, dateFormat)
        val resultGallery = getDate.call(mMapperMock, feedTypeGallery)
        assertEquals(expectedGalleryDate, resultGallery)

        val resultVideo = getDate.call(mMapperMock, feedTypeVideo)
        assertEquals("September 15, 2022", resultVideo)

        val resultWebUrl = getDate.call(mMapperMock, feedTypeWebUrl)
        assertEquals("September 25, 2022", resultWebUrl)


        FeatureFeedsMicroSDK.initialize(
            context,
            dateFormatType = DateFormat.STANDARD_DATE,
            csHostUrl = "w12121212",
            csAccessToken = "q20289129128912",
            csApiKey = "21212121212asa",
            dateFormat = dateFormat,
            environment = "dev",
            appScheme = "bulls"
        )

        val resultNBA = getDate.call(mMapperMock, feedTypeNBA)
        assertEquals("March 07, 2023", resultNBA)

    }
}