package dev.inkremental.meta.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply

class InkrementalModuleMppPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        apply<InkrementalGenPlugin>()
        apply<MavenPublishPlugin>()

        configureAndroid()
        configureDependencies()
        configureBintray()
        //val arts = configureAndroidArtifacts()

        afterEvaluate {
            /*extensions.getByType<InkrementalMetaExtension>().modules.forEach {
                when (it.type) {
                    InkrementalType.SDK -> {
                        registerPublication("Sdk17", "Sdk17", prop("POM_ARTIFACT_SDK17_ID")!!, arts)
                        registerPublication("Sdk19", "Sdk19", prop("POM_ARTIFACT_SDK19_ID")!!, arts)
                        registerPublication("Sdk21", "Sdk21", prop("POM_ARTIFACT_SDK21_ID")!!, arts)
                    }
                    InkrementalType.LIBRARY ->
                        registerPublication(it.camelCaseName, "", prop("POM_ARTIFACT_ID")!!, arts)
                }
            }*/
            dumpPublications()
        }
    }
}
