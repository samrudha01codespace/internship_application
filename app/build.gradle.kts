plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace  = "com.samrudha.bidai"
    compileSdk = 37

    defaultConfig {
        applicationId   = "com.samrudha.bidai"
        minSdk          = 24
        targetSdk       = 37
        versionCode     = 1
        versionName     = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = rootProject.file("app/release.keystore").takeIf { it.exists() }
            storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
                ?.ifEmpty { null }
                ?: System.getenv("KEYSTORE_PASSWORD")
                ?.ifEmpty { null }
                ?: ""
            keyAlias = System.getenv("ANDROID_ALIAS")
                ?.ifEmpty { null }
                ?: System.getenv("KEY_ALIAS")
                ?.ifEmpty { null }
                ?: ""
            keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
                ?.ifEmpty { null }
                ?: System.getenv("KEY_PASSWORD")
                ?.ifEmpty { null }
                ?: ""
        }
    }

    buildTypes {
        release {
            isMinifyEnabled   = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val releaseSigning = signingConfigs.getByName("release")
            signingConfig = if (releaseSigning.storeFile?.exists() == true
                && releaseSigning.storePassword?.isNotEmpty() == true
                && releaseSigning.keyAlias?.isNotEmpty() == true
                && releaseSigning.keyPassword?.isNotEmpty() == true
            ) releaseSigning else signingConfigs.getByName("debug")
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable         = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        jvmToolchain(21)
    }

    buildFeatures {
        compose    = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
