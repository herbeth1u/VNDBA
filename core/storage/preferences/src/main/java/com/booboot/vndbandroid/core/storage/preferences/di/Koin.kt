package com.booboot.vndbandroid.core.storage.preferences.di

import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import com.booboot.vndbandroid.core.storage.preferences.data.DataStorePreferencesDataSource
import com.booboot.vndbandroid.core.storage.preferences.data.PreferencesDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataStoreModule = module {
    single {
        ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() })
    }

    single {
        PreferenceDataStoreFactory.create(
            corruptionHandler = get()
        ) {
            get(named(DATASTORE_PREFERENCES_FILE_KEY))
        }
    }
    singleOf(::DataStorePreferencesDataSource) bind PreferencesDataSource::class
}

internal const val DATASTORE_PREFERENCES_FILE_KEY = "datastore_preferences_file"