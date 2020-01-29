import de.undercouch.gradle.tasks.download.Download

plugins {
    kotlin("jvm")
    application
    id("de.undercouch.download")
}

kotlin {
    dependencies {
        implementation(project(":introspect-descriptors"))
        implementation(kotlin("stdlib"))
        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("native-library-reader"))
    }
}

tasks {
    val kotlinRelease = "1.3.61"
    val distPlatform = "macos"
    val distName = "kotlin-native-$distPlatform-$kotlinRelease"
    val distType = "tar.gz"
    val homeTarget = File(System.getenv("HOME"), ".konan")
    val targetExists = homeTarget.exists()

    val buildDissecteeTarget = "$buildDir/dissectee"
    val dissecteeTarget = if(targetExists) homeTarget else File(buildDissecteeTarget)

    val downloadDissectee by creating(Download::class) {
        src("https://download-cf.jetbrains.com/kotlin/native/builds/releases/$kotlinRelease/$distPlatform/$distName.$distType")
        dest("$buildDissecteeTarget.$distType")
        overwrite(false)
    }
    val deployDissectee by creating(Copy::class) {
        dependsOn(downloadDissectee)

        from(tarTree(downloadDissectee.dest))
        into(dissecteeTarget)

        onlyIf { !targetExists }
    }

    val run by getting(JavaExec::class) {
        dependsOn(deployDissectee)
        args(dissecteeTarget.resolve(distName))
    }
}

application {
    mainClassName = "dev.inkremental.meta.introspect.ios.MainKt"
}

