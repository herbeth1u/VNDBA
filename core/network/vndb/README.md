# VNDB API

This module provides an easy access to the VNDB API: https://api.vndb.org/kana
This module should only be accessed by data modules (see https://developer.android.com/topic/modularization/patterns#data-modules).

### How to use

Include the Koin module:

```kotlin
val myModule = module {
    includes(vndbNetworkModule)
}
```

Then you can inject [VndbApi] as you need.
It is recommended to only include [VndbApi] as a constructor parameter to your DataSource.