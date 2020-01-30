plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dev.inkremental.module")
}

android {
    defaultConfig {
        missingDimensionStrategy("api", "sdk17")
    }
}

inkremental {
    androidLibrary("cardview-v7") {
        camelCaseName = "CardViewv7"
        srcPackage = "androidx.cardview"
        modulePackage = "dev.inkremental.dsl.androidx.cardview"
    }
}

dependencies {
    implementation(project(":anvil"))
    inkremental(project(":anvil", "inkrementalDefSdk17"))

    inkrementalGen("androidx.cardview:cardview:1.0.0")
}
