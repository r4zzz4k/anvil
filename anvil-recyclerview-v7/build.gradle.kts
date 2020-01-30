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
    androidLibrary("recyclerview-v7") {
        camelCaseName = "RecyclerViewv7"
        srcPackage = "androidx.recyclerview"
        modulePackage = "dev.inkremental.dsl.androidx.recyclerview"
        manualSetterName = "CustomRecyclerViewv7Setter"
    }
}

dependencies {
    implementation(project(":anvil"))
    inkremental(project(":anvil", "inkrementalDefSdk17"))

    inkrementalGen("androidx.recyclerview:recyclerview:1.1.0")
}
