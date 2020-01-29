import org.gradle.api.internal.artifacts.ArtifactAttributes
import org.gradle.api.internal.artifacts.transform.UnzipTransform

plugins {
    kotlin("jvm")
    application
}

val artifactType = ArtifactAttributes.ARTIFACT_FORMAT

val dissectee by configurations.creating {
    isTransitive = false
    attributes {
        attribute(artifactType, ArtifactTypeDefinition.DIRECTORY_TYPE)
    }
}

kotlin {
    dependencies {
        implementation(project(":introspect-descriptors"))
        implementation(kotlin("stdlib"))
        implementation(kotlin("stdlib-jdk8"))
        //implementation(kotlin("util-klib"))
        dissectee(kotlin("stdlib-js"))

        registerTransform(UnzipTransform::class) {
            from.attribute(artifactType, ArtifactTypeDefinition.JAR_TYPE)
            to.attribute(artifactType, ArtifactTypeDefinition.DIRECTORY_TYPE)
        }
    }
}

tasks {
    val dissecteeTarget = "$buildDir/dissectee"

    val deployDissectee by creating(Copy::class) {
        from(dissectee)
        into(dissecteeTarget)
    }
    val run by getting(JavaExec::class) {
        dependsOn(deployDissectee)
        args(dissecteeTarget)
    }
}

application {
    mainClassName = "dev.inkremental.meta.introspect.js.MainKt"
}
