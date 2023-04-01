package com.booboot.vndbandroid.app

import android.app.Application
import com.booboot.vndbandroid.app.di.appModule
import com.booboot.vndbandroid.app.di.setupKoin

class VndbApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setupKoin(appModule)
    }
}
