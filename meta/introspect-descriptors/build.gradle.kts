plugins {
    kotlin("jvm")
}

kotlin {
    dependencies {
        api(project(":model"))
        implementation(kotlin("stdlib"))
        //implementation(kotlin("util-klib"))
        implementation(kotlin("stdlib-jdk8"))
        api(kotlin("compiler"))
    }
}
