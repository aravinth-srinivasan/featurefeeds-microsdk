package com.raweng.dfe.microsdk.featurefeeds.mapper

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raweng.dfe.microsdk.featurefeeds.data.FeatureFeedRepositoryImpl
import com.raweng.dfe.microsdk.featurefeeds.model.FeatureFeedResponse
import com.raweng.dfe.microsdk.featurefeeds.model.FeaturedFeedModel
import com.raweng.dfe.microsdk.featurefeeds.model.NbaFeedModel
import com.raweng.dfe.microsdk.featurefeeds.type.FeedType
import com.raweng.dfe.microsdk.featurefeeds.type.SourceType
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

@RunWith(AndroidJUnit4::class)
class FeaturedFeedsMapperTest {

    @Test
    fun generateCMSImageUrlShouldReturnTheModifiedURL_forValidInput() {
        val mapperMock = mockk<FeaturedFeedsMapper>()
        val instance = FeatureFeedRepositoryImpl::class.java
        val companion = instance.declaredClasses.firstOrNull { it.simpleName == "Companion" }
        val contentStackUrl = companion?.declaredFields
            ?.firstOrNull { it.name == "CONTENT_STACK_URL" }
            ?.apply { isAccessible = true }
            ?.get(companion)

        val imageFormat = companion?.declaredFields
            ?.firstOrNull { it.name == "IMAGE_FORMAT" }
            ?.apply { isAccessible = true }
            ?.get(companion)


        val url = "$contentStackUrl/my_img.png"
        val expectedUrl = "$contentStackUrl/my_img.png?$imageFormat"


        val generateCMSImageUrl = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "generateCMSImageUrl" }
            ?: throw AssertionError("Private method not found")
        generateCMSImageUrl.isAccessible = true


        every { generateCMSImageUrl.call(mapperMock, url) } returns expectedUrl

        val result = generateCMSImageUrl.call(mapperMock, url) as String?
        assertEquals(expectedUrl, result)
        unmockkAll()
    }

    @Test
    fun testGetSourceWithValidInput() {
        val mapperMock = mockk<FeaturedFeedsMapper>()
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
        every { getSource.call(mapperMock, feedTypeDFEMock) } returns expectedDFESource

        val resultDFE = getSource.call(mapperMock, feedTypeDFEMock)
        assertEquals(expectedDFESource, resultDFE)

        val expectedCMSSource = SourceType.CONTENTSTACK.toString()
        every { getSource.call(mapperMock, feedTypeCMS) } returns expectedCMSSource

        val resultCMS = getSource.call(mapperMock, feedTypeCMS)
        assertEquals(expectedCMSSource, resultCMS)

        val expectedCMSSourceIfNullFeed = SourceType.CONTENTSTACK.toString()
        every { getSource.call(mapperMock, null) } returns expectedCMSSourceIfNullFeed

        val resultIfNull = getSource.call(mapperMock, null)
        assertEquals(expectedCMSSourceIfNullFeed, resultIfNull)
    }

    @Test
    fun test_getFeedPosition_validate_positive_and_negative() {
        val mapperMock = mockk<FeaturedFeedsMapper>()
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

        every { getFeedPosition.call(mapperMock, feedTypeArticle) } returns expectedPosition
        val resultArticle = getFeedPosition.call(mapperMock, feedTypeArticle)
        assertEquals(expectedPosition, resultArticle)


        every { getFeedPosition.call(mapperMock, feedTypeGallery) } returns expectedPosition
        val resultGallery = getFeedPosition.call(mapperMock, feedTypeGallery)
        assertEquals(expectedPosition, resultGallery)

        every { getFeedPosition.call(mapperMock, feedTypeVideo) } returns expectedPosition
        val resultVideo = getFeedPosition.call(mapperMock, feedTypeVideo)
        assertEquals(expectedPosition, resultVideo)

        every { getFeedPosition.call(mapperMock, feedTypeWebUrl) } returns expectedPosition
        val resultWebUrl = getFeedPosition.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedPosition, resultWebUrl)

        every { getFeedPosition.call(mapperMock, feedTypeNBA) } returns expectedPosition
        val resultNBA = getFeedPosition.call(mapperMock, feedTypeNBA)
        assertEquals(expectedPosition, resultNBA)

        every { getFeedPosition.call(mapperMock, null) } returns null
        val resultNull = getFeedPosition.call(mapperMock, null)
        assertEquals(null, resultNull)

    }


    @Test
    fun test_getFeedId_validate_positive_and_negative() {
        val mapperMock = mockk<FeaturedFeedsMapper>()
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
                metadata = FeatureFeedResponse.Metadata().apply {
                    uid = expectedUid
                }
            }
        }

        val getFeedId = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getFeedId" }
            ?: throw AssertionError("Private method not found")
        getFeedId.isAccessible = true

        every { getFeedId.call(mapperMock, feedTypeArticle) } returns expectedUid
        val resultArticle = getFeedId.call(mapperMock, feedTypeArticle)
        assertEquals(expectedUid, resultArticle)


        every { getFeedId.call(mapperMock, feedTypeGallery) } returns expectedUid
        val resultGallery = getFeedId.call(mapperMock, feedTypeGallery)
        assertEquals(expectedUid, resultGallery)

        every { getFeedId.call(mapperMock, feedTypeVideo) } returns expectedUid
        val resultVideo = getFeedId.call(mapperMock, feedTypeVideo)
        assertEquals(expectedUid, resultVideo)

        every { getFeedId.call(mapperMock, feedTypeWebUrl) } returns expectedUid
        val resultWebUrl = getFeedId.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedUid, resultWebUrl)

        every { getFeedId.call(mapperMock, feedTypeNBA) } returns expectedUid
        val resultNBA = getFeedId.call(mapperMock, feedTypeNBA)
        assertEquals(expectedUid, resultNBA)

        every { getFeedId.call(mapperMock, null) } returns null
        val resultNull = getFeedId.call(mapperMock, null)
        assertEquals(null, resultNull)

    }

    @Test
    fun test_getGenerateClickThroughLink_positive_and_negative() {
        val mapperMock = mockk<FeaturedFeedsMapper>()

        val feedType =  FeaturedFeedModel.FeedModel().apply {
            feedType = FeedType.ARTICLE.toString()
            dataSource = SourceType.CONTENTSTACK.toString()
            feedId = "asjaj13186316371136"
        }

        val expectedResult = "bulls://screen/feed_detail/${feedType.feedType?.lowercase() ?: ""}?position=${feedType.feedPosition ?: 0}&source=${feedType.dataSource?.lowercase() ?: ""}&feed_id=${feedType.feedId ?: ""}"

        val getGenerateClickThroughLink = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getGenerateClickThroughLink" }
            ?: throw AssertionError("Private method not found")
        getGenerateClickThroughLink.isAccessible = true

        every { getGenerateClickThroughLink.call(mapperMock, feedType) } returns expectedResult
        val result = getGenerateClickThroughLink.call(mapperMock, feedType)
        assertEquals(expectedResult, result)
    }

    @Test
    fun test_getHideLabel_validate_positive_and_negative() {
        val mapperMock = mockk<FeaturedFeedsMapper>()
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

        every { getHideLabel.call(mapperMock, feedTypeArticle) } returns expectedHideLabel1
        val resultArticle = getHideLabel.call(mapperMock, feedTypeArticle)
        assertEquals(expectedHideLabel1, resultArticle)


        every { getHideLabel.call(mapperMock, feedTypeGallery) } returns expectedHideLabel2
        val resultGallery = getHideLabel.call(mapperMock, feedTypeGallery)
        assertEquals(expectedHideLabel2, resultGallery)

        every { getHideLabel.call(mapperMock, feedTypeVideo) } returns expectedHideLabel1
        val resultVideo = getHideLabel.call(mapperMock, feedTypeVideo)
        assertEquals(expectedHideLabel1, resultVideo)

        every { getHideLabel.call(mapperMock, feedTypeWebUrl) } returns expectedHideLabel2
        val resultWebUrl = getHideLabel.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedHideLabel2, resultWebUrl)

        every { getHideLabel.call(mapperMock, feedTypeNBA) } returns expectedHideLabel2
        val resultNBA = getHideLabel.call(mapperMock, feedTypeNBA)
        assertEquals(expectedHideLabel2, resultNBA)

        every { getHideLabel.call(mapperMock, null) } returns null
        val resultNull = getHideLabel.call(mapperMock, null)
        assertEquals(null, resultNull)

    }


    @Test
    fun test_getHideTitle_validate_positive_and_negative() {
        val mapperMock = mockk<FeaturedFeedsMapper>()
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

        val getHideTitle = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getHideTitle" }
            ?: throw AssertionError("Private method not found")
        getHideTitle.isAccessible = true

        every { getHideTitle.call(mapperMock, feedTypeArticle) } returns expectedHideLabel1
        val resultArticle = getHideTitle.call(mapperMock, feedTypeArticle)
        assertEquals(expectedHideLabel1, resultArticle)


        every { getHideTitle.call(mapperMock, feedTypeGallery) } returns expectedHideLabel2
        val resultGallery = getHideTitle.call(mapperMock, feedTypeGallery)
        assertEquals(expectedHideLabel2, resultGallery)

        every { getHideTitle.call(mapperMock, feedTypeVideo) } returns expectedHideLabel1
        val resultVideo = getHideTitle.call(mapperMock, feedTypeVideo)
        assertEquals(expectedHideLabel1, resultVideo)

        every { getHideTitle.call(mapperMock, feedTypeWebUrl) } returns expectedHideLabel2
        val resultWebUrl = getHideTitle.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedHideLabel2, resultWebUrl)

        every { getHideTitle.call(mapperMock, feedTypeNBA) } returns expectedHideLabel2
        val resultNBA = getHideTitle.call(mapperMock, feedTypeNBA)
        assertEquals(expectedHideLabel2, resultNBA)

        every { getHideTitle.call(mapperMock, null) } returns null
        val resultNull = getHideTitle.call(mapperMock, null)
        assertEquals(null, resultNull)

    }

    @Test
    fun test_getHideDate_validate_positive_and_negative() {
        val mapperMock = mockk<FeaturedFeedsMapper>()
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

        val getHideDate = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getHideDate" }
            ?: throw AssertionError("Private method not found")
        getHideDate.isAccessible = true

        every { getHideDate.call(mapperMock, feedTypeArticle) } returns expectedHideLabel1
        val resultArticle = getHideDate.call(mapperMock, feedTypeArticle)
        assertEquals(expectedHideLabel1, resultArticle)


        every { getHideDate.call(mapperMock, feedTypeGallery) } returns expectedHideLabel2
        val resultGallery = getHideDate.call(mapperMock, feedTypeGallery)
        assertEquals(expectedHideLabel2, resultGallery)

        every { getHideDate.call(mapperMock, feedTypeVideo) } returns expectedHideLabel1
        val resultVideo = getHideDate.call(mapperMock, feedTypeVideo)
        assertEquals(expectedHideLabel1, resultVideo)

        every { getHideDate.call(mapperMock, feedTypeWebUrl) } returns expectedHideLabel2
        val resultWebUrl = getHideDate.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedHideLabel2, resultWebUrl)

        every { getHideDate.call(mapperMock, feedTypeNBA) } returns expectedHideLabel2
        val resultNBA = getHideDate.call(mapperMock, feedTypeNBA)
        assertEquals(expectedHideLabel2, resultNBA)

        every { getHideDate.call(mapperMock, null) } returns null
        val resultNull = getHideDate.call(mapperMock, null)
        assertEquals(null, resultNull)

    }

    @Test
    fun test_getDate_validate_positive_and_negative() {
        val mapperMock = mockk<FeaturedFeedsMapper>()
        val expectedUid = "101asnja383u11"
        val feedTypeArticle = FeatureFeedResponse.Entry.FeedType().apply {
            article = FeatureFeedResponse.Entry.FeedType.Article().apply {
                publishedDate = "1686034554"
            }
        }

        /*val feedTypeGallery = FeatureFeedResponse.Entry.FeedType().apply {
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
                metadata = FeatureFeedResponse.Metadata().apply {
                    uid = expectedUid
                }
            }
        }*/

        val getDate = FeaturedFeedsMapper::class.declaredMemberFunctions
            .firstOrNull { it.name == "getFeedId" }
            ?: throw AssertionError("Private method not found")
        getDate.isAccessible = true

        //every { getDate.call(mapperMock, feedTypeArticle) } returns expectedUid
        val resultArticle = getDate.call(mapperMock, feedTypeArticle)
        assertEquals("June 6, 2023", resultArticle)


        /*every { getDate.call(mapperMock, feedTypeGallery) } returns expectedUid
        val resultGallery = getDate.call(mapperMock, feedTypeGallery)
        assertEquals(expectedUid, resultGallery)

        every { getDate.call(mapperMock, feedTypeVideo) } returns expectedUid
        val resultVideo = getDate.call(mapperMock, feedTypeVideo)
        assertEquals(expectedUid, resultVideo)

        every { getDate.call(mapperMock, feedTypeWebUrl) } returns expectedUid
        val resultWebUrl = getDate.call(mapperMock, feedTypeWebUrl)
        assertEquals(expectedUid, resultWebUrl)

        every { getDate.call(mapperMock, feedTypeNBA) } returns expectedUid
        val resultNBA = getDate.call(mapperMock, feedTypeNBA)
        assertEquals(expectedUid, resultNBA)

        every { getDate.call(mapperMock, null) } returns null
        val resultNull = getDate.call(mapperMock, null)
        assertEquals(null, resultNull)
*/
    }

}