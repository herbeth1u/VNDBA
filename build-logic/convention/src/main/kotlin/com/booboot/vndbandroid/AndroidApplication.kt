/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.booboot.vndbandroid

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate

internal fun Project.configureAndroidApplication(
    applicationExtension: ApplicationExtension,
) {
    apply(from = "publish.gradle.kts")

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val versionCodeExtra: Int by extra
    val versionNameExtra: String by extra
    val releaseStoreFile: String by extra
    val releaseStorePassword: String by extra
    val releaseKeyAlias: String by extra
    val releaseKeyPassword: String by extra

    applicationExtension.apply {
        defaultConfig {
            versionCode = versionCodeExtra
            versionName = versionNameExtra
            targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
        }

        signingConfigs {
            create("release") {
                storeFile = file(releaseStoreFile)
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
        buildTypes {
            release {
                isMinifyEnabled = true
                isShrinkResources = true
                signingConfig = signingConfigs.getByName("release")

                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
}