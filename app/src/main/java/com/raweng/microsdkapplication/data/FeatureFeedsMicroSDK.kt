package com.raweng.microsdkapplication.data

open class FeatureFeedsMicroSDK<out T, in Config>(private val constructor: (Config) -> T) {

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: Config): T =
        instance ?: synchronized(this) {
            instance ?: constructor(arg).also { instance = it }
        }
}