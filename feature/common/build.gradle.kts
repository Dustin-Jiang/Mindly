plugins {
    alias(
        libs.plugins.android.library
    )
    alias(
        libs.plugins.kotlin.android
    )
    alias(
        libs.plugins.kotlin.compose
    )

    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace =
        "top.tsukino.llmdemo.feature.common"
    compileSdk =
        35

    defaultConfig {
        minSdk =
            24

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles(
            "consumer-rules.pro"
        )
    }

    buildTypes {
        release {
            isMinifyEnabled =
                false
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility =
            JavaVersion.VERSION_11
        targetCompatibility =
            JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget =
            "11"
    }
    buildFeatures {
        compose =
            true
    }
}

dependencies {
    api(project(":data"))

    api(libs.hilt.android)
    api(libs.androidx.hilt.navigation.compose)
    implementation(
        libs.media3.exoplayer
    )
    ksp(libs.hilt.android.compiler)

    api(libs.androidx.navigation.compose)

    api(
        platform(
            libs.androidx.compose.bom
        )
    )
    androidTestApi(
        platform(
            libs.androidx.compose.bom
        )
    )

    api(
        libs.androidx.core.ktx
    )
    api(
        libs.androidx.lifecycle.runtime.ktx
    )
    api(
        libs.androidx.activity.compose
    )
    api(
        libs.androidx.ui
    )
    api(
        libs.androidx.ui.graphics
    )
    api(
        libs.androidx.ui.tooling.preview
    )
    api(
        libs.androidx.material3
    )
    api(
        libs.androidx.material.icons.extended
    )
    testApi(
        libs.junit
    )
    androidTestApi(
        libs.androidx.junit
    )
    androidTestApi(
        libs.androidx.espresso.core
    )
    androidTestApi(
        libs.androidx.ui.test.junit4
    )
    debugApi(
        libs.androidx.ui.tooling
    )
    debugApi(
        libs.androidx.ui.test.manifest
    )
}