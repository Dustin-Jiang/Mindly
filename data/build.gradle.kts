plugins {
    alias(
        libs.plugins.android.library
    )
    alias(
        libs.plugins.kotlin.android
    )

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("androidx.room")
}

android {
    namespace =
        "top.tsukino.mindly.data"
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
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    api(project(":api"))

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    implementation("com.github.naman14:TAndroidLame:1.1") {
        exclude(group = "com.android.support", module = "support-compat")
    }

    implementation(
        libs.androidx.core.ktx
    )
    implementation(
        libs.androidx.appcompat
    )
    implementation(
        libs.material
    )
    testImplementation(
        libs.junit
    )
    androidTestImplementation(
        libs.androidx.junit
    )
    androidTestImplementation(
        libs.androidx.espresso.core
    )
}