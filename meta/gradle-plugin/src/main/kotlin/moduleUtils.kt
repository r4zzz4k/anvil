package dev.inkremental.meta.gradle

import com.android.build.gradle.LibraryExtension
import dev.inkremental.meta.model.div
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.*

fun Project.dumpPublications() {
    afterEvaluate {
        val publications = project.extensions.getByType<PublishingExtension>().publications.toList()
        logger.warn("===== Publications [${publications.size}]: ")
        publications.forEach {
            if(it is MavenPublication) {
                val artifacts = it.artifacts.toList()
                logger.warn("\t${it.name}: ${it.artifactId} [${artifacts.size}]")
                artifacts.forEach {
                    logger.warn("\t\t[${it.classifier}] ${it.file.absoluteFile.relativeTo(rootProject.projectDir).path}")
                }
            } else {
                logger.warn("\t${it.name} (non-maven)")
            }
        }
    }
}

fun Project.prop(key: String): String? =
    findProperty(key)?.let { it as String }

internal val compileSdk = 28
internal val minSdk = 17
internal val targetSdk = compileSdk

fun Project.configureAndroid() {
    pluginManager.withPlugin("com.android.library") {
        extensions.getByType<LibraryExtension>().apply {
            compileSdkVersion(compileSdk)

            defaultConfig {
                minSdkVersion(minSdk)
                targetSdkVersion(targetSdk)
            }

            sourceSets.all { java.srcDirs("src/$name/kotlin") }

            lintOptions.isAbortOnError = false
            testOptions.unitTests.isReturnDefaultValues = true

            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                sourceSets.all {
                    val root = "src/android${name.capitalize()}"
                    logger.error("========================= ANDROID SOURCE SET $root")
                    setRoot(root)
                    java.srcDirs("$root/kotlin")
                }
            }
        }
    }
}

fun Project.configureBintray() {
    val bintrayUser = System.getenv("BINTRAY_USER") ?: prop("bintrayUser")
    val bintrayApiKey = System.getenv("BINTRAY_API_KEY") ?: prop("bintrayApiKey")
    val bintrayRepo = System.getenv("BINTRAY_REPO") ?: prop("BINTRAY_REPO")
    if(bintrayUser != null && bintrayApiKey != null) {
        extensions.configure<PublishingExtension> {
            repositories {
                maven {
                    name = "Bintray"
                    url = uri("https://api.bintray.com/maven/" +
                            "${prop("BINTRAY_ORG")}/" +
                            bintrayRepo +
                            "/${prop("POM_PACKAGE_NAME")}/" +
                            //"${project.version}/" +
                            ";publish=1")
                    credentials {
                        username = bintrayUser
                        password = bintrayApiKey
                    }
                }
            }
        }
    }
}

fun Project.configureAndroidArtifacts(): List<Any> {
    val art = mutableListOf<Any>()
    val android = extensions.getByType<LibraryExtension>()

    val javadoc by tasks.creating(Javadoc::class) {
        source(android.sourceSets["main"].java.srcDirs)
        classpath += project.files(android.bootClasspath)
    }

    art += tasks.create<Jar>("sourcesJar") {
        from(android.sourceSets["main"].java.srcDirs)
        archiveClassifier.set("sources")
    }

    art += tasks.create<Jar>("javadocJar") {
        from(javadoc.destinationDir)
        archiveClassifier.set("javadoc")
    }

    android.libraryVariants.configureEach {
        val androidJar = android.sdkDirectory / "platforms" / compileSdk.toString() / "android.jar"
        val compileClasspath = getCompileClasspath(null)

        /*art += */tasks.create<Javadoc>("generate${name.capitalize()}Javadoc") {
            description = "Generates Javadoc for $name."
            source = compileClasspath.asFileTree
            classpath = files(compileClasspath, androidJar)
            extra["androidJar"] = androidJar
        }
    }

    art.forEach {
        artifacts.add("archives", it)
    }
    return art
}

fun Project.configureDependencies() {
    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        with(extensions.getByType<KotlinMultiplatformExtension>()) {
            android()

            sourceSets {
                getByName("commonMain").dependencies {
                    api(kotlin("stdlib-common"))
                }
                getByName("commonTest").dependencies {
                    api(kotlin("test-common"))
                    api(kotlin("test-annotations-common"))

                }
                getByName("androidMain").dependencies {
                    api(kotlin("stdlib-jdk8"))
                    implementation("androidx.annotation:annotation:1.1.0")
                }
                getByName("androidTest").dependencies {
                    implementation(kotlin("test-junit"))
                }
            }
        }
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.android") {
        dependencies {
            "api"("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
            "implementation"("androidx.annotation:annotation:1.1.0")
            "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit")
        }
    }
}

internal fun Project.fixPom(publication: MavenPublication) = publication.pom.withXml {
    with(asNode()) {
        appendNode("description", property("POM_DESCRIPTION"))
        appendNode("name", property("POM_NAME"))
        appendNode("url", property("POM_URL"))
        with(appendNode("scm")) {
            appendNode("url", property("POM_SCM_URL"))
            appendNode("connection", property("POM_SCM_CONNECTION"))
            appendNode("developerConnection", property("POM_SCM_DEV_CONNECTION"))
        }
        with(appendNode("licenses")) {
            with(appendNode("license")) {
                appendNode("name", property("POM_LICENCE_NAME"))
                appendNode("url", property("POM_LICENCE_URL"))
                appendNode("distribution", property("POM_LICENCE_DIST"))
            }
        }
    }
}

private fun KotlinTarget.mainDependencies(configure: KotlinDependencyHandler.() -> Unit) =
    compilations.findByName(KotlinCompilation.MAIN_COMPILATION_NAME)?.dependencies(configure)

private fun KotlinTarget.testDependencies(configure: KotlinDependencyHandler.() -> Unit) =
    compilations.findByName(KotlinCompilation.TEST_COMPILATION_NAME)?.dependencies(configure)
