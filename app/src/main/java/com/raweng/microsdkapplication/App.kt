package com.raweng.microsdkapplication

import android.app.Application
import com.raweng.dfe.DFEManager

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        DFEManager.init(this)
    }
}