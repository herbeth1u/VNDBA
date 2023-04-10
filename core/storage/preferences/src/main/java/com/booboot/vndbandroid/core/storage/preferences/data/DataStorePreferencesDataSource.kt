package com.booboot.vndbandroid.core.storage.preferences.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.map

class DataStorePreferencesDataSource(
    private val dataStore: DataStore<Preferences>
) : PreferencesDataSource {

    private fun <T> get(preferencesKey: Preferences.Key<T>) = dataStore.data.map { preferences ->
        preferences[preferencesKey]
    }

    override suspend fun getString(key: String) = get(stringPreferencesKey(key))
    override suspend fun getInt(key: String) = get(intPreferencesKey(key))
    override suspend fun getDouble(key: String) = get(doublePreferencesKey(key))
    override suspend fun getFloat(key: String) = get(floatPreferencesKey(key))
    override suspend fun getLong(key: String) = get(longPreferencesKey(key))
    override suspend fun getStringSet(key: String) = get(stringSetPreferencesKey(key))

    private suspend fun <T> put(preferencesKey: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun putString(key: String, value: String) =
        put(stringPreferencesKey(key), value)

    override suspend fun putInt(key: String, value: Int) = put(intPreferencesKey(key), value)
    override suspend fun putDouble(key: String, value: Double) =
        put(doublePreferencesKey(key), value)

    override suspend fun putFloat(key: String, value: Float) = put(floatPreferencesKey(key), value)
    override suspend fun putLong(key: String, value: Long) = put(longPreferencesKey(key), value)
    override suspend fun putStringSet(key: String, value: Set<String>) =
        put(stringSetPreferencesKey(key), value)

    private suspend fun <T> remove(preferencesKey: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(preferencesKey)
        }
    }

    override suspend fun removeString(key: String) = remove(stringPreferencesKey(key))
    override suspend fun removeInt(key: String) = remove(intPreferencesKey(key))
    override suspend fun removeDouble(key: String) = remove(doublePreferencesKey(key))
    override suspend fun removeFloat(key: String) = remove(floatPreferencesKey(key))
    override suspend fun removeLong(key: String) = remove(longPreferencesKey(key))
    override suspend fun removeStringSet(key: String) = remove(stringSetPreferencesKey(key))
}