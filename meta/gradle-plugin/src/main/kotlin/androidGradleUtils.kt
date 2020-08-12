package dev.inkremental.meta.gradle

import com.android.build.gradle.BaseExtension
import dev.inkremental.meta.model.div
import org.gradle.api.Project
import java.io.File

internal fun androidSdkJar(sdkDirectory: File, compileSdk: String) =
    sdkDirectory / "platforms" / compileSdk / "android.jar"

internal fun androidSdkJar(android: BaseExtension) =
    androidSdkJar(android.sdkDirectory, android.compileSdkVersion)

internal fun Project.requireAndroidSdkRoot(): File {
    val path = prop("sdk.dir")
        ?: env("ANDROID_SDK_ROOT")
        ?: env("ANDROID_HOME")
        ?: error(
            "Android SDK location is not defined. " +
            "Please put SDK path to either local.properties file to property sdk.dir " +
            "or pass it via ANDROID_SDK_ROOT environment variable."
        )
    return File(path)
}

internal fun Project.requireAndroidSdkJar(compileSdk: String): File =
    androidSdkJar(requireAndroidSdkRoot(), compileSdk).also {
        if(!it.exists()) error(
            "Jar file for SDK $compileSdk is not found at ${it.absolutePath}. " +
            "Please download platform $compileSdk via SDK manager or by invoking " +
            "the following command from shell: " +
            "sdkmanager \"platforms;android-$compileSdk\""
        )
    }
