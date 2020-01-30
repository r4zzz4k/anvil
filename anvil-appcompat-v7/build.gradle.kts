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
	androidLibrary("appcompat-v7") {
		camelCaseName = "AppCompatv7"
        srcPackage = "androidx.appcompat"
        modulePackage = "dev.inkremental.dsl.androidx.appcompat"
		manualSetterName = "CustomAppCompatv7Setter"
		quirks = mutableMapOf(
			// Depends on internal class, can be re-enabled when metadata is implemented
			"androidx.appcompat.widget.ActionBarContextView" to mapOf(
				"__viewAlias" to false
			),

			// Unknown issues
			"androidx.appcompat.widget.AppCompatTextView" to mapOf(
				"setTextFuture:java.util.concurrent.Future" to false
			),

			// Access limited to the same library
			"androidx.appcompat.widget.ContentFrameLayout" to mapOf(
				"setAttachListener:androidx.appcompat.widget.ContentFrameLayout.OnAttachListener" to false
			),

			// Deprecated view; framework nas android.widget.SearchView since API 11
			"androidx.appcompat.widget.SearchView" to mapOf(
				"__viewAlias" to false
			)
		)
	}
}

dependencies {
	implementation(project(":anvil"))
	inkremental(project(":anvil", "inkrementalDefSdk17"))

	inkrementalGen("androidx.appcompat:appcompat:1.1.0")
}
