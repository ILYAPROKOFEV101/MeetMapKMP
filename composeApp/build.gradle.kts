import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleGmsGoogleServices)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqlDelight)
}
val ktor_version = "3.0.1"

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.ilya")
        }
    }
}



kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    @OptIn(ExperimentalWasmDsl::class)

    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.ui)
            implementation(libs.androidx.ui.graphics)
            implementation(libs.androidx.ui.tooling.preview)
            implementation(libs.androidx.material3)
            implementation(libs.firebase.auth)
            implementation(libs.androidx.material3.android)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.lifecycle.runtime.compose.android)
            implementation(libs.androidx.appcompat)
            implementation(libs.material)
            implementation(libs.androidx.activity)
            implementation(libs.androidx.constraintlayout)
            implementation(libs.play.services.location)
            implementation(libs.androidx.ui.text.google.fonts)
            implementation(libs.firebase.crashlytics.buildtools)
            implementation(libs.transport.runtime)
            implementation(libs.protolite.well.known.types)
            implementation(libs.firebase.database.ktx)
            implementation(libs.androidx.legacy.support.v4)
            implementation(libs.androidx.recyclerview)
            implementation(libs.androidx.room.common)
            implementation (libs.androidx.fragment.ktx.v185)
            implementation(libs.foundation.layout.android)
            implementation(libs.androidx.ui.test.android)
            implementation(libs.firebase.database)
            implementation(libs.firebase.storage) // Убедитесь, что версия соответствует последней
            implementation(libs.coil.compose) // Coil для загрузки изображений (Compose)
            implementation(libs.firebase.auth.ktx) // Firebase Authentication (Kotlin)
            implementation(libs.play.services.auth) // Google Play Services Auth
            implementation(libs.androidx.material3.v121) // Замените на актуальную версию
            implementation(libs.play.services.maps)// для googlemaps
            implementation (libs.accompanist.permissions) //
            implementation (libs.core)
            implementation (libs.androidx.lifecycle.runtime.ktx.v261)
            // CALENDAR
            implementation (libs.calendar)
            // CLOCK
            implementation (libs.clock)
            // MEterial 3 icon
            implementation(libs.androidx.material3.v131) // Или более новая версия
            implementation(libs.androidx.material.icons.core)
            implementation(libs.androidx.material.icons.extended)
            implementation(libs.play.services.location) // Or the latest version
            implementation (libs.material.v150)
            implementation (libs.material)
            // Server setings
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)        // Движок CIO
            implementation(libs.ktor.client.android)       // Движок Android
            implementation(libs.ktor.client.logging)       // Логирование
            implementation(libs.ktor.client.json)          // Работа с JSON
            implementation(libs.ktor.client.serialization) // Сериализация
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.serialization.json.v160) // JSON сериализация от kotlinx
// retrofit
            implementation (libs.retrofit)
            implementation (libs.converter.gson)
            implementation (libs.logging.interceptor)
            implementation (libs.kotlinx.coroutines.core)
            implementation (libs.kotlinx.coroutines.android)
            implementation (libs.okhttp)
            implementation (libs.converter.gson)
            implementation(libs.jetbrains.kotlinx.serialization.json) // Используйте актуальную версию kotlinx-serialization-json
            implementation (libs.konfetti.xml)
            implementation (libs.glide) // load image
            implementation (libs.glide) // load image
            implementation (libs.okhttp.v4120) // Websoket liber for conect
            implementation (libs.logging.interceptor)
            implementation (libs.glide)
            implementation (libs.compiler)
            implementation(libs.androidx.room.runtime)
            // optional - Kotlin Extensions and Coroutines support for Room
            implementation(libs.androidx.room.ktx)

            // optional - RxJava2 support for Room
            implementation(libs.androidx.room.rxjava2)

            // optional - RxJava3 support for Room
            implementation(libs.androidx.room.rxjava3)
            // optional - Guava support for Room, including Optional and ListenableFuture
            implementation(libs.androidx.room.guava)

            // optional - Paging 3 Integration
            implementation(libs.androidx.room.paging)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            implementation (libs.androidx.core.splashscreen)
            implementation ("com.google.firebase:firebase-auth-ktx:23.1.0") // Проверьте актуальную версию
            implementation("io.ktor:ktor-client-core:3.0.2") // Основной модуль клиента
            implementation("io.ktor:ktor-client-okhttp:3.0.1") // Движок OkHttp для Android
            implementation("io.ktor:ktor-client-logging:3.0.1") // Для логирования
            implementation("io.ktor:ktor-client-content-negotiation:3.0.2") // Для сериализации
            implementation("io.ktor:ktor-client-json:2.3.4") // Работа с JSON (при необходимости)
            implementation("io.ktor:ktor-client-serialization:2.3.4") // Сериализация (если нужно)
            implementation("io.ktor:ktor-client-plugins:2.3.4") // Плагины, включая HttpT

            implementation(platform("io.github.jan-tennert.supabase:bom:3.0.2"))

            implementation("io.github.jan-tennert.supabase:storage-kt:3.0.2")
            // sqldelight
            implementation(libs.koin.android)
            implementation(libs.sqldelight.driver.android)
            implementation("com.jakewharton.timber:timber:5.0.1")



        }
        commonMain.dependencies {
            implementation("io.ktor:ktor-client-core:3.0.1")
            implementation("io.ktor:ktor-client-websockets:3.0.1")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            implementation("io.ktor:ktor-client-websockets:$ktor_version")
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(platform("io.github.jan-tennert.supabase:bom:3.0.2"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

            implementation("io.github.jan-tennert.supabase:storage-kt:3.0.2")
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.datetime)

            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.no.arg)
            

        }
        iosMain.dependencies {
            implementation(libs.sqldelight.ios)
            implementation("io.ktor:ktor-client-darwin:2.3.4")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "com.ilya.meetmapkmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.ilya.meetmapkmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.firebase.auth)
    implementation(libs.androidx.runtime.livedata)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.ilya.meetmapkmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.ilya.meetmapkmp"
            packageVersion = "1.0.0"
        }
    }
}
