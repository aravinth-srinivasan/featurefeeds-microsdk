package com.raweng.dfe.microsdk.featurefeeds.model
import kotlinx.serialization.Serializable

@Serializable
data class GalleryModel(
    var caption: String? = null,
    var imageTitle: String? = null,
    var url: String? = null,
    var imageType: String? = null,
): java.io.Serializable