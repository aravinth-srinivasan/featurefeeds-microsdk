package com.raweng.dfe.microsdk.featurefeeds.model

data class Config(
    var cmsApiKey: String? = "",
    var cmsAccessToken: String? = "",
    var cmsUrl: String? = "",
    var dfeSportsKey: String? = "",
    var cmsApiVersion: String? = "",
    var dfeApiVersion: String? = "",
    var contentType: String? = "",
    var environment: String? = "",
)