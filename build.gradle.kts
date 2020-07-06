plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.3.72" apply false
    id("org.jetbrains.kotlin.android") version "1.3.72" apply false
    id("com.android.application") version "3.6.3" apply false
    id("com.android.library") version "3.6.3" apply false
    id("dev.inkremental.module") version "snapshot" apply false
}

fun loadProperties(fileName: String) =
    java.util.Properties()
        .also { props ->
            try {
                file("$rootDir/$fileName").inputStream().use {
                    props.load(it)
                }
            } catch(e: java.io.FileNotFoundException) {
                // do nothing
            }
        }
        .forEach { name, value -> rootProject.extra[name as String] = value }

loadProperties("local.properties")

subprojects {
    extra["junit_version"] = "4.12"
    extra["mockito_version"] = "2.23.0"

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

val mainSubprojects = listOf(
    "anvil",
    "anvil-appcompat-v7",
    "anvil-gridlayout-v7",
    "anvil-recyclerview-v7",
    "anvil-cardview-v7",
    "anvil-design",
    "anvil-support-v4",
    "anvil-constraintlayout",
    "anvil-yogalayout"
)
fun registerGlobalTask(name: String, subprojectTask: String) = tasks.register<Task>(name) {
    setDependsOn(mainSubprojects.map { ":$it:$subprojectTask" })
}

registerGlobalTask("generateAndCheck", "check")
registerGlobalTask("generateAndPublishLocally", "publishToMavenLocal")
registerGlobalTask("generateAndPublish", "publishAllPublicationsToBintrayRepository")
