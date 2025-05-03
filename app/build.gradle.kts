plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

dependencies {
        // Import the BoM for the Firebase platform
        implementation(platform("com.google.firebase:firebase-bom:33.13.0"))

        // Add the dependency for the Firebase Authentication library
        // When using the BoM, you don't specify versions in Firebase library dependencies
        implementation("com.google.firebase:firebase-auth")

        // Also add the dependencies for the Credential Manager libraries and specify their versions
        implementation("androidx.credentials:credentials:1.3.0")
        implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
        implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
}

android {
    namespace = "com.suchet.smartFridge"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.suchet.smartFridge"
        minSdk = 34
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.archcoreTesting)
    implementation(libs.androidxTestCore)
    implementation(libs.roomTesting)

}