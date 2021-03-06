import dev.inkremental.meta.model.DslTransformer.*

plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("dev.inkremental.module")
}

inkremental {
	androidSdk(listOf(17, 19, 21)) {
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
				"setInputExtras" to false,
				"setMinWidth" to false	// we handle it
			),
			// Exception must be checked in this attribute setter
			"android.view.View" to mapOf(
				"setElevation" to false // we handle it
			),

			"android.widget.Switch" to mapOf(
				"__viewAlias" to "SwitchView",
				"setSwitchMinWidth" to false	// we handle it
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
		transformers = mapOf(
				"android.view.View" to mapOf(
						"setMinimumHeight" to listOf(IntToDpTransformer),
						"setMinimumWidth" to listOf(IntToDpTransformer),
						"setBackground" to listOf(NullableForSureTransformer)
				)
		)
	}
}

dependencies {
	val junit_version: String by project.extra
	val mockito_version: String by project.extra

	implementation("androidx.annotation:annotation:1.1.0")
	testImplementation("junit:junit:$junit_version")
	testImplementation("org.mockito:mockito-core:$mockito_version")
}
