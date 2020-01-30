package dev.inkremental.meta.gradle

import dev.inkremental.meta.model.InkrementalType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.*

class InkrementalModulePlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        apply<InkrementalGenPlugin>()
        apply<MavenPublishPlugin>()

        configureAndroid()
        configureDependencies()
        configureBintray()
        val arts = configureAndroidArtifacts()

        afterEvaluate {
            extensions.getByType<InkrementalMetaExtension>().modules.forEach {
                when (it.type) {
                    InkrementalType.SDK -> {
                        registerPublication("Sdk17", "Sdk17", prop("POM_ARTIFACT_SDK17_ID")!!, arts)
                        registerPublication("Sdk19", "Sdk19", prop("POM_ARTIFACT_SDK19_ID")!!, arts)
                        registerPublication("Sdk21", "Sdk21", prop("POM_ARTIFACT_SDK21_ID")!!, arts)
                    }
                    InkrementalType.LIBRARY ->
                        registerPublication(it.camelCaseName, "", prop("POM_ARTIFACT_ID")!!, arts)
                }
            }
            dumpPublications()
        }

    }

    private fun Project.registerPublication(name: String, bundleName: String, artifactId: String, arts: List<Any>) =
        registerAndroidPublication(
            name,
            artifactId,
            arts +
                    configurations.getByName(CONFIGURATION_MODULE_DEF + name).artifacts +
                    tasks.getByName("bundle${bundleName}ReleaseAar")
        )

    private fun Project.registerAndroidPublication(
        name: String,
        artifactId: String,
        artifacts: List<Any>) {
        project.extensions.getByType<PublishingExtension>()
            .publications
            .register<MavenPublication>(name) {
                this.groupId = project.group.toString()
                this.artifactId = artifactId
                this.version = project.version.toString()
                artifacts.forEach { artifact(it) }
                fixPom(this)
            }
    }
}
