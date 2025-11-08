plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.pruebafinal"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.pruebafinal"
        minSdk = 29
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    implementation(libs.androidx.room.ktx)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Firebase (BOM para manejar versiones)
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))

    // Autenticación
    implementation("com.google.firebase:firebase-auth-ktx")

    // Firestore
    implementation("com.google.firebase:firebase-firestore-ktx")

    //Storage (para fotos o imágenes)
    implementation("com.google.firebase:firebase-storage-ktx")

    //Analytics (opcional pero recomendado)
    implementation("com.google.firebase:firebase-analytics-ktx")

    //Navegación en Compose
    implementation("androidx.navigation:navigation-compose:2.8.2")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")





}