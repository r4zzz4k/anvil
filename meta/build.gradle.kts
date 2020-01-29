import java.util.*

plugins {
    kotlin("jvm") version "1.3.61" apply false
    kotlin("plugin.serialization") version "1.3.61" apply false
    id("de.undercouch.download") version "4.0.4" apply false
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

    val GROUP: String by project
    val VERSION_NAME: String by project

    group = GROUP
    version = VERSION_NAME
}
