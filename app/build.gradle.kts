plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.cstre.landingpage"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cstre.landingpage"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    dependencies {
        val room_version = "2.7.0"

        implementation("androidx.room:room-runtime:$room_version")

        // If this project only uses Java source, use the Java annotationProcessor
        // No additional plugins are necessary
        annotationProcessor("androidx.room:room-compiler:$room_version")

        // optional - Kotlin Extensions and Coroutines support for Room
        implementation("androidx.room:room-ktx:$room_version")

        // optional - RxJava2 support for Room
        implementation("androidx.room:room-rxjava2:$room_version")

        // optional - RxJava3 support for Room
        implementation("androidx.room:room-rxjava3:$room_version")

        // optional - Guava support for Room, including Optional and ListenableFuture
        implementation("androidx.room:room-guava:$room_version")

        // optional - Test helpers
        testImplementation("androidx.room:room-testing:$room_version")

        // optional - Paging 3 Integration
        implementation("androidx.room:room-paging:$room_version")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}