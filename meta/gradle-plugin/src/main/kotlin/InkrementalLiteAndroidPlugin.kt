package dev.inkremental.meta.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.closureOf

class InkrementalLiteAndroidPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        apply<JavaPlugin>()
        //apply<MavenPublishPlugin>()

        val sdkDir = requireAndroidSdkRoot()
        val compileSdk = reqProp("inkremental.module.compilesdk")

        dependencies(closureOf<DependencyHandler> {
            add("compileOnly", file(androidSdkJar(sdkDir, compileSdk)))
        })
    }
}
