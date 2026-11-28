import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

// Load local properties containing the Google Maps API key
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

// Read the local properties file if it exists
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

android {
    namespace = "com.example.medimapireland"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.medimapireland"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        // Pass the Google Maps API key to AndroidManifest.xml
        manifestPlaceholders["MAPS_API_KEY"] =
            localProperties.getProperty("MAPS_API_KEY", "")


    }
    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    // Enable View Binding to access XML views from Kotlin
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    // Gson library for converting Kotlin objects to and from JSON
    implementation("com.google.code.gson:gson:2.11.0")

    // RecyclerView displays healthcare facilities in a scrollable list
    implementation("androidx.recyclerview:recyclerview:1.4.0")

    // Google Maps SDK for displaying healthcare facility locations
    implementation("com.google.android.gms:play-services-maps:19.2.0")

    // Google Location Services for accessing the user's current location
    implementation("com.google.android.gms:play-services-location:21.3.0")

    //dependecies for testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}