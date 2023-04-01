# Ktor module

This module implements Ktor as an HTTP client and provides a [CoreHttpClient] interface to interact
with it.
Other modules including this module should not import nor access nor have any kind of interaction
with Ktor directly.
If Ktor has to be replaced by another HTTP client, only this module should be affected.

### How to use

Include the Koin module:

```kotlin
val myModule = module {
    includes(networkModule)
}
```

Then you can inject [CoreHttpClient] as you need.
It is recommended, for every API implementation, to create a :core:network:<api_name> module to
provide an interface to every API implementation for the rest of the app.