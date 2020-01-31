plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("dev.inkremental.module.multiplatform")
}

kotlin {
    jvm("mock")

    sourceSets {
        getByName("mockMain").dependencies {
            api(kotlin("stdlib-jdk8"))
            implementation("androidx.annotation:annotation:1.1.0")
        }
        getByName("mockTest").dependencies {
            implementation(kotlin("test-junit"))
        }
    }
}
