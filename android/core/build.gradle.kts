plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("dev.inkremental.module.multiplatform")
}

android {
    flavorDimensions("api")
    productFlavors {
        create("sdk17") {
            minSdkVersion(17)
        }
        create("sdk19") {
            minSdkVersion(19)
        }
        create("sdk21") {
            minSdkVersion(21)
        }
    }
}

inkremental {
    androidSdk {
        srcPackage = "android"
        modulePackage = "dev.inkremental.dsl.android"
        manualSetterName = "CustomSdkSetter"
        quirks = mutableMapOf(
            // This requires lots of type checks, skipping this factory method for now
            "android.widget.AutoCompleteTextView" to mapOf(
                "setAdapter" to false
            ),

            // Block setAdapter(ListAdapter) and setAdapter(SpinnerAdapter), leave only parent setAdapter(Adapter) instead
            "android.widget.AbsListView" to mapOf(
                "setAdapter" to false
            ),
            "android.widget.ListView" to mapOf(
                "setAdapter" to false
            ),
            "android.widget.GridView" to mapOf(
                "setAdapter" to false
            ),
            "android.widget.AbsSpinner" to mapOf(
                "setAdapter" to false
            ),
            "android.widget.Spinner" to mapOf(
                "setAdapter" to false
            ),
            "android.widget.ExpandableListView" to mapOf(
                "setAdapter:android.widget.ListAdapter" to false
            ),

            // Exception must be checked in this attribute setter
            "android.widget.TextView" to mapOf(
                "setText:kotlin.CharSequence" to false,
                "setTextSize" to false, // broken: uses "sp" unit by default
                "setInputExtras" to false
            ),

            "android.widget.Switch" to mapOf(
                "__viewAlias" to "SwitchView"
            ),

            // This will remove the whole builder, so no factory method will be generated
            "android.renderscript.RSSurfaceView" to mapOf(
                "__viewAlias" to false
            ),

            // This will remove the whole builder, so no factory method will be generated
            "android.renderscript.RSTextureView" to mapOf(
                "__viewAlias" to false
            ),

            // TODO This is a static method
            "android.webkit.WebView" to mapOf(
                "setWebContentsDebuggingEnabled" to false
            )
        )
    }
}

kotlin.sourceSets.getByName("androidTest").dependencies {
    implementation("org.mockito:mockito-core:${extra["mockito_version"]}")
}
