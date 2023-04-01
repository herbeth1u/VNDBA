package com.booboot.vndbandroid.core.network.vndb.di

import com.booboot.vndbandroid.core.network.ktor.di.networkModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val vndbNetworkModule = module {
    includes(networkModule)
    singleOf(::VndbApi)
}