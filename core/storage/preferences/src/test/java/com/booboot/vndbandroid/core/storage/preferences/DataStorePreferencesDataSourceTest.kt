package com.booboot.vndbandroid.core.storage.preferences

import com.booboot.vndbandroid.core.storage.preferences.data.DataStorePreferencesDataSource
import com.booboot.vndbandroid.core.storage.preferences.di.dataStoreModule
import com.booboot.vndbandroid.core.test.kotlin.KotlinUnitTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.koin.core.component.get
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DataStorePreferencesDataSourceTest : KotlinUnitTest() {
    @get:Rule(order = 0)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    override val modules = listOf(dataStoreModule, module {
        single(named("datastore_preferences_file")) {
            File(tmpFolder.root, "datastore_v4.preferences_pb")
        }
    })

    private lateinit var dataSource: DataStorePreferencesDataSource

    @Before
    fun setup() {
        dataSource = get()
    }

    @Test
    fun `getting inexistent preference key should provide null`() =
        runTest(mainDispatcherRule.testDispatcher) {
            assertNull(dataSource.getString("inexistent_key").first())
        }

    @Test
    fun `getting a key after putting a value should retrieve the value`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // GIVEN a value has been inserted for a specific key
            val key = "existing_key"
            dataSource.putString(key, "foo")

            // WHEN we want to retrieve the key, THEN we should retrieve the inserted value
            assertEquals("foo", dataSource.getString(key).first())
        }

    @Test
    fun `getting the latest value multiple times on the same flow`() =
        runTest(mainDispatcherRule.testDispatcher) {
            // GIVEN a value has been inserted for a specific key
            val key = "existing_key"
            dataSource.putString(key, "foo")

            // WHEN we want to retrieve the key through a Flow
            val flow = dataSource.getString(key)

            // THEN we can call it multiple times without recreating it
            assertEquals("foo", flow.first())
            assertEquals("foo", flow.first())
        }

    // TODO
    // Cannot write any more tests because DataStore is bugged on Windows when updating data
    // more than once:
    // https://github.com/googlecodelabs/android-datastore/issues/48
}