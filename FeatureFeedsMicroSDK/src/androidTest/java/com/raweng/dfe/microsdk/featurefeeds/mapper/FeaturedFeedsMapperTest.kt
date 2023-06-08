package com.raweng.dfe.microsdk.featurefeeds.mapper

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raweng.dfe.microsdk.featurefeeds.FeatureFeedsMicroSDK
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.model.GalleryModel
import com.raweng.dfe.microsdk.featurefeeds.model.NbaFeedModel
import com.raweng.dfe.microsdk.featurefeeds.type.DateFormat
import com.raweng.dfe.microsdk.featurefeeds.type.FeedType
import com.raweng.dfe.microsdk.featurefeeds.type.SourceType
import com.raweng.dfe.microsdk.featurefeeds.utils.Utils
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

@RunWith(AndroidJUnit4::class)
class FeaturedFeedsMapperTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val dateFormat = "MMMM dd, yyyy"

    @Test
    fun test_generateCMSImageUrlShouldReturnTheModifiedURL_forValidInput() {

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
                    nbaFeedModel = NbaFeedModel().apply {
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
                    publishedDate = "2022-09-25T14:47:16.000Z"
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

    @Test
    fun test_getTitle_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val expectedArticleTitle = "Title 1"
        val expectedGalleryTitle = "Gallery Title"
        val expectedVideoTitle = "Video Title"
        val expectedWebUrlTitle = "WebUrl Title"
        val expectedNBATitle = "NBA Title"

        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                title = expectedArticleTitle
            }
        }

        val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery().apply {
                title = expectedGalleryTitle
            }
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                title = expectedVideoTitle
            }
        }

        val feedTypeWebUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl().apply {
                title = expectedWebUrlTitle
            }
        }

        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel = NbaFeedModel().apply {
                        title = expectedNBATitle
                    }
                }
            }
        }

        val getTitle = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getTitle" }
            ?: throw AssertionError("Private method not found")
        getTitle.isAccessible = true

        val resultArticle = getTitle.call(mapperMock, feedTypeArticle)
        assertEquals(expectedArticleTitle, resultArticle)

        val resultGallery = getTitle.call(mapperMock, feedTypeGallery)
        assertEquals(expectedGalleryTitle, resultGallery)

        val resultVideo = getTitle.call(mapperMock, feedTypeVideo)
        assertEquals(expectedVideoTitle, resultVideo)

        val resultWebUrl = getTitle.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedWebUrlTitle, resultWebUrl)

        val resultNBA = getTitle.call(mapperMock, feedTypeNBA)
        assertEquals(expectedNBATitle, resultNBA)

        val resultNull = getTitle.call(mapperMock, null)
        assertEquals(null, resultNull)
    }

    @Test
    fun test_getDFEFeedType_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val expectedArticleFeed = "article"
        val expectedGalleryFeed = "gallery"
        val expectedVideoFeed = "video"
        val expectedWebUrlFeed = "web_url"
        val expectedNBAFeed = "article"

        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article()
        }

        val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery()
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video()
        }

        val feedTypeWebUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl()
        }

        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel = NbaFeedModel().apply {
                        feedType = expectedNBAFeed
                    }
                }
            }
        }

        val getFeedType = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getFeedType" }
            ?: throw AssertionError("Private method not found")
        getFeedType.isAccessible = true

        val resultArticle = getFeedType.call(mapperMock, feedTypeArticle)
        assertEquals(expectedArticleFeed.uppercase(), resultArticle)

        val resultGallery = getFeedType.call(mapperMock, feedTypeGallery)
        assertEquals(expectedGalleryFeed.uppercase(), resultGallery)

        val resultVideo = getFeedType.call(mapperMock, feedTypeVideo)
        assertEquals(expectedVideoFeed.uppercase(), resultVideo)

        val resultWebUrl = getFeedType.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedWebUrlFeed.uppercase(), resultWebUrl)

        val resultNBA = getFeedType.call(mapperMock, feedTypeNBA)
        assertEquals(expectedNBAFeed.uppercase(), resultNBA)

        val resultNull = getFeedType.call(mapperMock, null)
        assertEquals(null, resultNull)
    }

    @Test
    fun test_getAdditionalContent_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val expectedArticleAdditionalContent = "article additional content"
        val expectedGalleryAdditionalContent = "gallery additional content"
        val expectedVideoAdditionalContent = "video additional content"
        val expectedWebUrlAdditionalContent = "web url additional content"
        val expectedNBAAdditionalContent = "article additional conentent"

        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                additionalContent = expectedArticleAdditionalContent
            }
        }

        val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery().apply {
                additionalContent = expectedGalleryAdditionalContent
            }
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                additionalContent = expectedVideoAdditionalContent
            }
        }

        val feedTypeWebUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl().apply {
                additionalContent = expectedWebUrlAdditionalContent
            }
        }

        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel = NbaFeedModel().apply {
                        additionalContent = expectedNBAAdditionalContent
                    }
                }
            }
        }

        val getAdditionalContent = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getAdditionalContent" }
            ?: throw AssertionError("Private method not found")
        getAdditionalContent.isAccessible = true

        val resultArticle = getAdditionalContent.call(mapperMock, feedTypeArticle)
        assertEquals(expectedArticleAdditionalContent, resultArticle)

        val resultGallery = getAdditionalContent.call(mapperMock, feedTypeGallery)
        assertEquals(expectedGalleryAdditionalContent, resultGallery)

        val resultVideo = getAdditionalContent.call(mapperMock, feedTypeVideo)
        assertEquals(expectedVideoAdditionalContent, resultVideo)

        val resultWebUrl = getAdditionalContent.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedWebUrlAdditionalContent, resultWebUrl)

        val resultNBA = getAdditionalContent.call(mapperMock, feedTypeNBA)
        assertEquals(expectedNBAAdditionalContent, resultNBA)

        val resultNull = getAdditionalContent.call(mapperMock, null)
        assertEquals(null, resultNull)
    }

    @Test
    fun test_getContent_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val expectedArticleContent = "article content"
        val expectedGalleryContent = "gallery content"
        val expectedVideoContent = "video content"
        val expectedWebUrlContent = "web url content"
        val expectedNBAContent = "article content"

        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                content = expectedArticleContent
            }
        }

        val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery().apply {
                content = expectedGalleryContent
            }
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                content = expectedVideoContent
            }
        }

        val feedTypeWebUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl().apply {
                content = expectedWebUrlContent
            }
        }

        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel = NbaFeedModel().apply {
                        content = expectedNBAContent
                    }
                }
            }
        }

        val getContent = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getContent" }
            ?: throw AssertionError("Private method not found")
        getContent.isAccessible = true

        val resultArticle = getContent.call(mapperMock, feedTypeArticle)
        assertEquals(expectedArticleContent, resultArticle)

        val resultGallery = getContent.call(mapperMock, feedTypeGallery)
        assertEquals(expectedGalleryContent, resultGallery)

        val resultVideo = getContent.call(mapperMock, feedTypeVideo)
        assertEquals(expectedVideoContent, resultVideo)

        val resultWebUrl = getContent.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedWebUrlContent, resultWebUrl)

        val resultNBA = getContent.call(mapperMock, feedTypeNBA)
        assertEquals(expectedNBAContent, resultNBA)

        val resultNull = getContent.call(mapperMock, null)
        assertEquals(null, resultNull)
    }

    @Test
    fun test_getGalleryImages_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val mFeedGalleryIteratorMock =
            mockk<Iterator<FeatureFeedResponse.Entry.FeedType.Gallery.GalleryImage?>>()
        val mockImage = mockk<FeatureFeedResponse.Entry.FeedType.Gallery.GalleryImage>()
        val image = mockk<FeatureFeedResponse.Image>()

        val feedType = mockk<FeatureFeedResponse.Entry.FeedType>()
        val gallery = mockk<FeatureFeedResponse.Entry.FeedType.Gallery>()
        val galleryImages = mockk<List<FeatureFeedResponse.Entry.FeedType.Gallery.GalleryImage?>>()


        every { feedType.article } returns null
        every { feedType.video } returns null
        every { feedType.webUrl } returns null
        every { feedType.nbaFeeds } returns null
        every { feedType.gallery } returns gallery
        every { feedType.gallery?.images } returns galleryImages
        every { galleryImages.size } returns 1
        every { galleryImages.iterator() } returns mFeedGalleryIteratorMock
        every { mFeedGalleryIteratorMock.hasNext() } returns true andThen (false)
        every { mFeedGalleryIteratorMock.next() } returns mockImage andThen (null)
        every { mockImage.image } returns image
        every { mockImage.caption } returns "image cap"
        every { image.url } returns "https:\\/\\/images.contentstack.io\\/v3\\/assets\\/blt8f37077c228fc690\\/blt0712523e58263e47\\/62f0d6255c954177895af0e2\\/British_Grand_Prix.png"
        every { image.title } returns "image"
        every { image.contentType } returns "image/png"


        val getGalleryImages = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getGalleryImages" }
            ?: throw AssertionError("Private method not found")
        getGalleryImages.isAccessible = true


        val resultGallery = getGalleryImages.call(mapperMock, feedType)
        val expectedList = listOf(
            GalleryModel(
                caption = mockImage.caption,
                imageTitle = image.title,
                url = image.url,
                imageType = image.contentType,
            )
        )
        assertEquals(expectedList, resultGallery)

        val nbaFeedResponse = mockk<FeatureFeedResponse.Entry>()
        val nbaMapperMock = FeaturedFeedsMapper(nbaFeedResponse)
        val nbaModel = mockk<FeatureFeedResponse.Entry.FeedType.NbaFeeds>()
        val nbaModelFEED = mockk<FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds>()
        val nbaFeedModel = mockk<NbaFeedModel>()
        val mediaList = mockk<List<NbaFeedModel.FeedMedia?>>()
        val media = mockk<NbaFeedModel.FeedMedia>()
        val mediaIterator = mockk<Iterator<NbaFeedModel.FeedMedia?>>()


        every { feedType.article } returns null
        every { feedType.video } returns null
        every { feedType.webUrl } returns null
        every { feedType.nbaFeeds } returns nbaModel
        every { feedType.gallery } returns null
        every { nbaModel.nbaFeeds } returns nbaModelFEED
        every { nbaModel.nbaFeeds?.nbaFeedModel } returns nbaFeedModel
        every { nbaModel.nbaFeeds?.nbaFeedModel?.media } returns mediaList
        every { mediaList.size } returns 1
        every { mediaList.iterator() } returns mediaIterator
        every { mediaIterator.hasNext() } returns true andThen false
        every { mediaIterator.next() } returns media andThen null
        every { media.source } returns "https://www.tets.com/imag.png"
        every { media.caption } returns "Test cap"
        every { media.title } returns "Test title"
        every { media.type } returns "image"

        val expectedModel = GalleryModel(
            caption = media.caption,
            imageTitle = media.title,
            url = media.source,
            imageType = media.type
        )


        val resultNBA = getGalleryImages.call(nbaMapperMock, feedType)
        assertEquals(listOf(expectedModel), resultNBA)

        val resultNull = getGalleryImages.call(mapperMock, FeatureFeedResponse.Entry.FeedType())
        assertEquals(listOf<FeatureFeedResponse.Entry.FeedType>(), resultNull)
    }

    @Test
    fun test_getVideoLink_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val expectedVideoLink = "https://www.cms.video.mp4"
        val expectedNBAVideoLink = "https://www.dfe.video.mp4"

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                videoLink = expectedVideoLink
            }
        }


        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel = NbaFeedModel().apply {
                        video = listOf(
                            NbaFeedModel.Video().apply {
                                url = expectedNBAVideoLink
                            }
                        )
                    }
                }
            }
        }

        val getVideoLink = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getVideoLink" }
            ?: throw AssertionError("Private method not found")
        getVideoLink.isAccessible = true

        val resultVideo = getVideoLink.call(mapperMock, feedTypeVideo)
        assertEquals(expectedVideoLink, resultVideo)


        val resultNBA = getVideoLink.call(mapperMock, feedTypeNBA)
        assertEquals(expectedNBAVideoLink, resultNBA)

        val resultNull = getVideoLink.call(mapperMock, null)
        assertEquals("", resultNull)
    }


    @Test
    fun test_getLink_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val expectedVideoLink = "https://www.cms.video.mp4"
        val expectedNBAVideoLink = "https://www.dfe.video.mp4"
        val expectedWebUrlLink = "https://www.google.com"

        val article = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                link = null
            }
        }

        val gallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery().apply {
                link = null
            }
        }

        val webUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl().apply {
                link = expectedWebUrlLink
            }
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video().apply {
                videoLink = expectedVideoLink
            }
        }


        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel = NbaFeedModel().apply {
                        video = listOf(
                            NbaFeedModel.Video().apply {
                                url = expectedNBAVideoLink
                            }
                        )
                    }
                }
            }
        }

        val feedTypeNBANoVideo = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel = NbaFeedModel().apply {
                        video = null
                    }
                }
            }
        }

        val getLink = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getLink" }
            ?: throw AssertionError("Private method not found")
        getLink.isAccessible = true


        val resultArticle = getLink.call(mapperMock, article)
        assertEquals("", resultArticle)

        val resultGallery = getLink.call(mapperMock, gallery)
        assertEquals("", resultGallery)

        val resultWebUrl = getLink.call(mapperMock, webUrl)
        assertEquals(expectedWebUrlLink, resultWebUrl)

        val resultVideo = getLink.call(mapperMock, feedTypeVideo)
        assertEquals(expectedVideoLink, resultVideo)

        val resultNBA = getLink.call(mapperMock, feedTypeNBA)
        assertEquals(expectedNBAVideoLink, resultNBA)

        val resultNBAIfNoVideo = getLink.call(mapperMock, feedTypeNBANoVideo)
        assertEquals("", resultNBAIfNoVideo)

        val resultNull = getLink.call(mapperMock, null)
        assertEquals("", resultNull)
    }

    @Test
    fun test_getSpotlightImage_validate_positive_and_negative() {
        val response = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(response)

        val expectedArticleSpotlight = "https://www.cms.video.jpg"

        val article = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                spotlightImage = FeatureFeedResponse.Image().apply {
                    url = expectedArticleSpotlight
                }
            }
        }

        val gallery = FeatureFeedResponse.Entry.FeedType().apply {
            gallery = FeatureFeedResponse.Entry.FeedType.Gallery()
        }

        val webUrl = FeatureFeedResponse.Entry.FeedType().apply {
            webUrl = FeatureFeedResponse.Entry.FeedType.WebUrl()
        }

        val feedTypeVideo = FeatureFeedResponse.Entry.FeedType().apply {
            video = FeatureFeedResponse.Entry.FeedType.Video()
        }


        val feedTypeNBA = FeatureFeedResponse.Entry.FeedType().apply {
            nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds().apply {
                nbaFeeds = FeatureFeedResponse.Entry.FeedType.NbaFeeds.NbaFeeds().apply {
                    nbaFeedModel = NbaFeedModel()
                }
            }
        }

        val getSpotlightImage = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getSpotlightImage" }
            ?: throw AssertionError("Private method not found")
        getSpotlightImage.isAccessible = true


        val resultArticle = getSpotlightImage.call(mapperMock, article)
        assertEquals(expectedArticleSpotlight, resultArticle)

        val resultGallery = getSpotlightImage.call(mapperMock, gallery)
        assertEquals("", resultGallery)

        val resultWebUrl = getSpotlightImage.call(mapperMock, webUrl)
        assertEquals("", resultWebUrl)

        val resultVideo = getSpotlightImage.call(mapperMock, feedTypeVideo)
        assertEquals("", resultVideo)


        val resultNBA = getSpotlightImage.call(mapperMock, feedTypeNBA)
        assertEquals("", resultNBA)

        val resultNull = getSpotlightImage.call(mapperMock, null)
        assertEquals(null, resultNull)
    }

    @Test
    fun test_getFeedList_validate_positive_and_negative() {

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

        val mockResponse = mockk<FeatureFeedResponse.Entry>()
        val mockFeedModel = mockk<FeaturedFeedModel>()
        val mockExpectedFeedModel = FeaturedFeedModel.FeedModel()
        val mapperMock = FeaturedFeedsMapper(mockResponse)
        val mockArticle = mockk<FeatureFeedResponse.Entry.FeedType>()
        val mFeedMock = mockk<List<FeatureFeedResponse.Entry.FeedType?>>()
        val mFeedArticleIteratorMock = mockk<Iterator<FeatureFeedResponse.Entry.FeedType?>>()
        val mockImage = mockk<FeatureFeedResponse.Image>()
        val metadata = mockk<FeatureFeedResponse.Metadata>()


        val getFeedList = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getFeedList" }
            ?: throw AssertionError("Private method not found")
        getFeedList.isAccessible = true


        every { mockArticle.article } returns mockk()
        every { mockArticle.gallery } returns null
        every { mockArticle.nbaFeeds } returns null
        every { mockArticle.webUrl } returns null
        every { mockArticle.video } returns null
        every { mockFeedModel.uid } returns "csbtjh112324345"
        every { metadata.uid } returns "ashajh112324345"
        every { mockImage.url } returns "https://images.contentstack.io/v3/assets/blt8f37077c228fc690/blt7e08400340714769/62906003e2c498542888299b/img1.png"
        every { mockArticle.article?.position } returns 0
        every { mockArticle.article?.hideDate } returns false
        every { mockArticle.article?.hideLabel } returns false
        every { mockArticle.article?.hideTitle } returns false
        every { mockArticle.article?.content } returns "content data"
        every { mockArticle.article?.additionalContent } returns "adddtionla content data"
        every { mockArticle.article?.link } returns "https://test.com/data"
        every { mockArticle.article?.metadata } returns metadata
        every { mockArticle.article?.halfWidthImage } returns mockImage
        every { mockArticle.article?.title } returns "Article title"
        every { mockArticle.article?.publishedDate } returns "2023-05-25T19:32:42.000Z"
        every { mockArticle.article?.spotlightImage } returns null


        mockExpectedFeedModel.hideDate = false
        mockExpectedFeedModel.hideLabel = false
        mockExpectedFeedModel.hideTitle = false
        mockExpectedFeedModel.content = "content data"
        mockExpectedFeedModel.additionalContent = "adddtionla content data"
        mockExpectedFeedModel.link = "https://test.com/data"
        mockExpectedFeedModel.thumbnail = mockImage.url
        mockExpectedFeedModel.title = "Article title"
        mockExpectedFeedModel.date = "May 25, 2023"
        mockExpectedFeedModel.spotlightImage = null
        mockExpectedFeedModel.feedType = "article"
        mockExpectedFeedModel.label = "ARTICLE"
        mockExpectedFeedModel.feedId = metadata.uid
        mockExpectedFeedModel.feedPosition = 0
        mockExpectedFeedModel.dataSource = SourceType.CONTENTSTACK.toString()
        mockExpectedFeedModel.clickthroughLink =
            "bulls://screen/feed_detail/article?position=0&source=contentstack&feed_id=${metadata.uid}&uid=${mockFeedModel.uid}"
        mockExpectedFeedModel.galleryImages = listOf()
        mockExpectedFeedModel.spotlightImage = ""
        mockExpectedFeedModel.author = null

        every { mFeedMock.iterator() } returns mFeedArticleIteratorMock
        every { mFeedMock.size } returns 2
        every { mockResponse.feedType } returns mFeedMock
        every { mFeedArticleIteratorMock.hasNext() } returns true andThen (false)
        every { mFeedArticleIteratorMock.next() } returns mockArticle andThen (null)

        val resultArticle = getFeedList.call(mapperMock, mockFeedModel)
        assertEquals(listOf(mockExpectedFeedModel), resultArticle)


        val resultNull = getFeedList.call(mapperMock, null)
        assertEquals(listOf<FeaturedFeedModel.FeedModel>(), resultNull)
    }

    @Test
    fun test_getUpdatedAtDate_validate_positive_and_negative() {

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

        val mockResponse = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(mockResponse)
        val getUpdatedAtDate = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getUpdatedAtDate" }
            ?: throw AssertionError("Private method not found")
        getUpdatedAtDate.isAccessible = true

        every { mockResponse.updatedAt } returns "2023-06-07T11:34:18.052Z"

        val result = getUpdatedAtDate.call(mapperMock)
        assertEquals("YESTERDAY", result)

        val resultIfNull = getUpdatedAtDate.call(FeaturedFeedsMapper(FeatureFeedResponse.Entry()))
        assertEquals(null, resultIfNull)


    }

    @Test
    fun test_getFeatureFeeds_validate_positive_and_negative() {

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

        val mockResponse = mockk<FeatureFeedResponse.Entry>()
        val mapperMock = FeaturedFeedsMapper(mockResponse)

        every { mockResponse.updatedAt } returns "2023-06-07T11:34:18.052Z"
        every { mockResponse.title } returns "Row 1"
        every { mockResponse.uid } returns "abctest12134"
        every { mockResponse.order } returns 0
        every { mockResponse.feedType } returns null

        val expectedModel = FeaturedFeedModel(
            title = mockResponse.title,
            isoDate = mockResponse.updatedAt,
            updatedAt = "YESTERDAY",
            uid = mockResponse.uid,
            order = mockResponse.order,
            feeds = listOf()
        )

        val result = mapperMock.getFeatureFeeds()
        assertEquals(expectedModel, result)

        FeatureFeedsMicroSDK.initialize(
            context,
            dateFormatType = DateFormat.STANDARD_DATE,
            csHostUrl = "w12121212",
            csAccessToken = "q20289129128912",
            csApiKey = "21212121212asa",
            dateFormat = "dd/MM/yyyy hh:mm a",
            environment = "dev",
            appScheme = "bulls",
        )


        val expectedModel1 = expectedModel.copy(updatedAt = "07/06/2023 11:34 am")
        val result1 = mapperMock.getFeatureFeeds()
        assertEquals(expectedModel1, result1)

        every { mockResponse.updatedAt } returns null
        every { mockResponse.title } returns null
        every { mockResponse.uid } returns null
        every { mockResponse.order } returns 0
        every { mockResponse.feedType } returns null

        val resultIfNull = mapperMock.getFeatureFeeds()
        assertEquals(
            FeaturedFeedModel(
                title = null,
                isoDate = null,
                updatedAt = null,
                uid = null,
                order = 0,
                feeds = listOf()
            ), resultIfNull
        )
    }
}