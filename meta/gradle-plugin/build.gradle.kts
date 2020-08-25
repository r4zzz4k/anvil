plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    jcenter()
    google()
}

dependencies {
    implementation(project(":model"))
    implementation(project(":introspect-android"))
    /*implementation(project(":introspect-ios")) {
        exclude("org.jetbrains.kotlin", "kotlin-native-library-reader")
    }*/

    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
    compileOnly("com.android.tools.build:gradle:3.6.3")
}

gradlePlugin {
    plugins {
        register("gen") {
            id = "dev.inkremental.gen"
            implementationClass = "dev.inkremental.meta.gradle.InkrementalGenPlugin"
        }
        register("module") {
            id = "dev.inkremental.module"
            implementationClass = "dev.inkremental.meta.gradle.InkrementalModulePlugin"
        }
    }
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
