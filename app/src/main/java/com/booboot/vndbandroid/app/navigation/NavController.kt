package com.booboot.vndbandroid.app.navigation

import androidx.navigation.NavController

val NavController.currentRoute: String?
    get() = currentBackStackEntry?.destination?.route