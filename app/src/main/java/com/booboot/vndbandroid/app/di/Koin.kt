package com.booboot.vndbandroid.app.di

import android.app.Application
import com.booboot.vndbandroid.feature.vnlist.di.vnListModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val appModule = module {
    includes(vnListModule)

    single(named("datastore_preferences_file")) {
        File(androidContext().filesDir, "datastore_v4.preferences_pb")
    }
}

fun Application.setupKoin(vararg modules: Module) = startKoin {
    androidLogger()
    androidContext(this@setupKoin)
    modules(*modules)
}
