// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false // Ensure Kotlin Android plugin is included
    alias(libs.plugins.kotlin.kapt) apply false    // Include kapt plugin
}

