package com.raweng.dfe.microsdk.featurefeeds.model

data class FullWidthImage(
    val _version: Int?,
    val content_type: String?,
    val created_at: String?,
    val created_by: String?,
    val file_size: String?,
    val filename: String?,
    val is_dir: Boolean?,
    val parent_uid: String?,
    val publish_details: PublishDetails?,
    val tags: List<Any>?,
    val title: String?,
    val uid: String?,
    val updated_at: String?,
    val updated_by: String?,
    val url: String?
)