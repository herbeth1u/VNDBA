package com.booboot.vndbandroid.app.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = module {
}

fun Application.setupKoin(vararg modules: Module) = startKoin {
    androidLogger()
    androidContext(this@setupKoin)
    modules(*modules)
}
