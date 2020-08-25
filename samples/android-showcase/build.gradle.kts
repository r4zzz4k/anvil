plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        applicationId = "dev.inkremental.sample"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"

        missingDimensionStrategy("dev.inkremental.variant.anvil", "sdk-17")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets.all {
        java.srcDirs("src/$name/kotlin")
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation(project(":anvil"))
    implementation(project(":anvil-support-v4"))
    implementation(project(":anvil-appcompat-v7"))
    implementation(project(":anvil-constraintlayout"))
    implementation(project(":anvil-yogalayout"))
    implementation(project(":anvil-recyclerview-v7"))

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
