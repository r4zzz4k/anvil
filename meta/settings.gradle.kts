pluginManagement {
    repositories {
        mavenLocal()
        maven(url = "https://dl.bintray.com/inkremental/maven")
        gradlePluginPortal()
        google()
        jcenter()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "inkremental-meta"

include(
    ":model",
    ":introspect-android",
    //":introspect-ios",
    ":gradle-plugin"
)
