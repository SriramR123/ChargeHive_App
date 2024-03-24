plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

android {
    namespace = "com.citsri.chargify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.citsri.chargify"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures{
        buildConfig=true
        viewBinding = true
    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.material)

    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.converter.gson)

//    implementation(libs.logging.interceptor)
//    implementation(libs.okhttps)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.mapbox.maps:android:11.2.1")
    implementation ("com.airbnb.android:lottie:6.4.0")

    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}