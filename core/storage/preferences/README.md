# Preferences module

This module provides flows to local preferences through the [PreferencesDataSource] interface.
This interface is currently implemented with a DataStore implementation (but is subject to change).

### How to use

Include the Koin module and define the file that should be used to store the preferences:

```kotlin
val dataStoreModule = module {
    includes(networkModule)

    single(named("datastore_preferences_file")) {
        File("<persistent_folder>", "<preferences_file_name>")
    }
}
```

Then inject [PreferencesDataSource] in your Data Source...:
(A Data Source separate from [PreferencesDataSource] should always be created to ensure the
separation of concerns when accessing Preferences)

```kotlin
class MyDataSource(val preferencesDataSource: PreferencesDataSource) {
    fun getData() = preferencesDataSource.getString("<key>")
}
```

...and consume your data source from your Repository:

```kotlin
class MyRepository(val myDataSource: MyDataSource) {
    val data = myDataSource.getData() // from an attribute here, NOT from a method
}
```

The Data Source's Flow should always be consumed as an attribute, so that subsequent updates on the
preferences can automatically be collected.

This [data] Flow can be used as the source of truth in your Repository when using
multiple Data Sources (
see https://developer.android.com/topic/architecture/data-layer#source-of-truth).