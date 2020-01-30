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
    androidLibrary("gridlayout-v7") {
        camelCaseName = "GridLayoutv7"
        srcPackage = "androidx.gridlayout"
        modulePackage = "dev.inkremental.dsl.androidx.gridlayout"
    }
}

dependencies {
    implementation(project(":anvil"))
    inkremental(project(":anvil", "inkrementalDefSdk17"))

    inkrementalGen("androidx.gridlayout:gridlayout:1.0.0")
}
