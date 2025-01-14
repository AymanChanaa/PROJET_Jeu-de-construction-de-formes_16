plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) // Apply Kotlin Android plugin
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.example.shape_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shape_project"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // Kotlin-specific configuration
    kotlinOptions {
        jvmTarget = "1.8" // Set Kotlin JVM target to 1.8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Glide for image loading and caching
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    // RecyclerView for displaying the images
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    // AndroidX libraries for improved support
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
