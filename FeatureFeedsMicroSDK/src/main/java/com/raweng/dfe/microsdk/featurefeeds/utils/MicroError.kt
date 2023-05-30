package com.raweng.dfe.microsdk.featurefeeds.utils

data class MicroError(val errorMsg: String = "", val exception: Exception? = null) : Throwable(errorMsg)