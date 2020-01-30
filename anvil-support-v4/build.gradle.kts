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
    androidLibrary("support-core-ui") {
        camelCaseName = "SupportCoreUi"
        srcPackage = "androidx.core"
        modulePackage = "dev.inkremental.dsl.androidx.core"
        quirks = mutableMapOf(
            // Deprecated view; framework nas android.widget.Space since API 14
            "androidx.legacy.widget.Space" to mapOf(
                "__viewAlias" to false
            ),
            "androidx.core.widget.NestedScrollView" to mapOf(
                "setOnScrollChangeListener" to false
            )
        )
    }
}

dependencies {
    implementation(project(":anvil"))
    inkremental(project(":anvil", "inkrementalDefSdk17"))

    inkrementalGen("androidx.coordinatorlayout:coordinatorlayout:1.0.0")
    inkrementalGen("androidx.core:core:1.1.0")
    inkrementalGen("androidx.drawerlayout:drawerlayout:1.0.0")
    inkrementalGen("androidx.legacy:legacy-support-core-ui:1.0.0")
    inkrementalGen("androidx.slidingpanelayout:slidingpanelayout:1.0.0")
    inkrementalGen("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")
    inkrementalGen("androidx.viewpager:viewpager:1.0.0")
}


