@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.googleGmsGoogleServices)
}

android {
    namespace = "com.example.appmanager"
    compileSdk = 35

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
    }

    defaultConfig {
        applicationId = "com.example.appmanager"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

//RxJava
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("io.reactivex.rxjava3:rxjava:3.0.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")
    //paper
    implementation("io.github.pilgr:paperdb:2.7.1")

    //gson
    implementation("com.google.code.gson:gson:2.8.9")

    //eventBus
    implementation("org.greenrobot:eventbus:3.2.0")

    //viewpager2
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    //Motion Toast
    implementation("com.github.Spikeysanju:MotionToast:1.4")

    //Lottie
    implementation ("com.airbnb.android:lottie:6.4.0")

    //firebase
    implementation ("com.google.firebase:firebase-auth:23.2.0")
    implementation ("androidx.credentials:credentials:1.5.0-rc01")
    implementation ("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    //firebase cloud messagging
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    implementation("com.google.firebase:firebase-analytics:22.0.0")
    implementation("com.google.firebase:firebase-firestore:25.0.0")

    //OAuth 2.0
    implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")

    //MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //ImageSlider
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")
}

apply(plugin = "com.google.gms.google-services")