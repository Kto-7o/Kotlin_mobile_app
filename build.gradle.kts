// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
//    alias(libs.plugins.kotlin.kapt) apply false
}
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:9.2.1") // или более новая версия
        // или для самых свежих возможностей в 2026 году:
        // classpath("com.android.tools.build:gradle:9.1.0")
    }
}