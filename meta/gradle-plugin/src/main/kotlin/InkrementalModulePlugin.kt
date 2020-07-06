package dev.inkremental.meta.gradle

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.component.SoftwareComponent
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.*

class InkrementalModulePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        apply<InkrementalGenPlugin>()
        apply<MavenPublishPlugin>()

        val minSdk = reqProp("inkremental.module.minsdk").toInt()
        val targetSdk = reqProp("inkremental.module.targetsdk").toInt()
        val compileSdk = reqProp("inkremental.module.compilesdk").toInt()

        val android = extensions.getByType<LibraryExtension>()

        with(android) {
            compileSdkVersion(compileSdk)

            defaultConfig {
                minSdkVersion(minSdk)
                targetSdkVersion(targetSdk)
            }

            lintOptions.isAbortOnError = false
            testOptions.unitTests.isReturnDefaultValues = true
        }

        pluginManager.withPlugin("org.jetbrains.kotlin.android") {
            with(android) {
                sourceSets.all { java.srcDirs("src/$name/kotlin") }
            }
            dependencies {
                "api"("org.jetbrains.kotlin:kotlin-stdlib-jdk7")
                "testImplementation"("org.jetbrains.kotlin:kotlin-test")
                "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit")
            }
        }

        val bintrayUser = envOrProp("BINTRAY_USER")
        val bintrayApiKey = envOrProp("BINTRAY_API_KEY")
        val bintrayRepo = envOrProp("BINTRAY_REPO")
        if(bintrayUser != null && bintrayApiKey != null) {
            extensions.configure<PublishingExtension> {
                repositories {
                    maven {
                        name = "Bintray"
                        url = bintrayUri(
                            reqProp("inkremental.module.bintray.organization"),
                            bintrayRepo!!,
                            reqProp("inkremental.module.bintray.package"),
                            publish = true
                        )
                        credentials {
                            username = bintrayUser
                            password = bintrayApiKey
                        }
                    }
                }
            }
        }

        // FIXME generate proper -javadoc (and -sources) JARs
        /*android.libraryVariants.configureEach {
            val androidJar = android.sdkDirectory / "platforms" / compileSdk.toString() / "android.jar"
            val compileClasspath = getCompileClasspath(null)

            val javadoc = tasks.register<Javadoc>("generate${name.capitalize()}Javadoc") {
                description = "Generates Javadoc for $name."
                source = compileClasspath.asFileTree
                classpath = files(compileClasspath, androidJar)
                extra["androidJar"] = androidJar
            }

            tasks.register<Jar>("generate${name.capitalize()}JavadocJar") {
                dependsOn(javadoc)
                from(javadoc.get().destinationDir)
                archiveClassifier.set("javadoc")
            }
        }*/

        fun registerPublication(name: String, artifactId: String, verion: String) {
            logger.debug("registerPublication: $name")
            registerPublicationImpl(
                name,
                artifactId,
                verion,
                components.getByName(androidComponentName(name)),
                //tasks.getByName("generate${name}ReleaseJavadocJar"),
                *configurations.getByName(moduleDefConfigurationName(name)).artifacts.toTypedArray()
            )
        }

        val extension = extensions.getByType<InkrementalMetaExtension>()

        afterEvaluate {
            extension.modules.configureEach {
                val pubVersion = (if (version.isNotEmpty()) "$version-" else "") + project.version.toString()
                registerPublication(dslName, reqProp("inkremental.module.pom.artifactid"), pubVersion)
            }
        }
    }
}

private fun Project.registerPublicationImpl(
    name: String,
    artifactId: String,
    version: String,
    vararg artifacts: Any) =
    project.extensions.getByType<PublishingExtension>()
        .publications
        .register<MavenPublication>(name) {
            this.groupId = project.group.toString()
            this.artifactId = artifactId
            this.version = version
            artifacts.forEach {
                if(it is SoftwareComponent) {
                    from(it)
                } else {
                    artifact(it)
                }
            }
            fixPom(this)
        }

private fun Project.fixPom(publication: MavenPublication) = publication.pom.apply {
    packaging = "aar"
    description.set(prop("inkremental.module.pom.description"))
    name.set(prop("inkremental.module.pom.name"))
    url.set(prop("inkremental.module.pom.url"))
    scm {
        url.set(prop("inkremental.module.pom.scm.url"))
        connection.set(prop("inkremental.module.pom.scm.connection"))
        developerConnection.set(prop("inkremental.module.pom.scm.dev.connection"))
    }
    licenses {
        license {
            name.set(prop("inkremental.module.pom.license.name"))
            url.set(prop("inkremental.module.pom.license.url"))
            distribution.set(prop("inkremental.module.pom.license.distribution"))
        }
    }
}
