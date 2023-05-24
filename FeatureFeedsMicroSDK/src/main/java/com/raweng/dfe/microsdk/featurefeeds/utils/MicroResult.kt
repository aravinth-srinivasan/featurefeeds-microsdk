package com.raweng.dfe.microsdk.featurefeeds.utils



sealed class MicroResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : MicroResult<T>()
    data class Failure(val error: MicroError) : MicroResult<Nothing>()
}