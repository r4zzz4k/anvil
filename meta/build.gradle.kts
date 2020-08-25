import java.util.*

plugins {
    id("com.android.application") version "3.6.3" apply false
    kotlin("jvm") version "1.3.72" apply false
    kotlin("plugin.serialization") version "1.3.72" apply false
}

fun loadProperties(fileName: String) =
    Properties()
        .also { props ->
            try {
                file("$rootDir/../$fileName").inputStream().use {
                    props.load(it)
                }
            } catch(e: java.io.FileNotFoundException) {
                // do nothing
            }
        }
        .forEach { name, value -> rootProject.extra[name as String] = value }

loadProperties("gradle.properties")
loadProperties("local.properties")

subprojects {
    repositories {
        mavenLocal()
        maven(url = "https://dl.bintray.com/inkremental/maven")
        google()
        jcenter()
    }

    group = project.property("inkremental.module.group")!!
    version = project.property("inkremental.module.version")!!.toString() +
            System.getenv("inkremental.module.version.suffix")?.let { "-$it" }
}

tasks.register("clean") {
    dependsOn(subprojects.mapNotNull { it.tasks.findByName("clean") })
}
