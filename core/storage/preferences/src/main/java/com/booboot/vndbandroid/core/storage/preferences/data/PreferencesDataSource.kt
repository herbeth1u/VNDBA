package com.booboot.vndbandroid.core.storage.preferences.data

import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {
    suspend fun getString(key: String): Flow<String?>
    suspend fun getInt(key: String): Flow<Int?>
    suspend fun getDouble(key: String): Flow<Double?>
    suspend fun getFloat(key: String): Flow<Float?>
    suspend fun getLong(key: String): Flow<Long?>
    suspend fun getStringSet(key: String): Flow<Set<String>?>

    suspend fun putString(key: String, value: String)
    suspend fun putInt(key: String, value: Int)
    suspend fun putDouble(key: String, value: Double)
    suspend fun putFloat(key: String, value: Float)
    suspend fun putLong(key: String, value: Long)
    suspend fun putStringSet(key: String, value: Set<String>)

    suspend fun removeString(key: String)
    suspend fun removeInt(key: String)
    suspend fun removeDouble(key: String)
    suspend fun removeFloat(key: String)
    suspend fun removeLong(key: String)
    suspend fun removeStringSet(key: String)
}