package dev.inkremental.meta.gradle

import com.android.build.gradle.LibraryExtension
import dev.inkremental.meta.model.*
import org.gradle.api.*
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE
import org.gradle.api.component.SoftwareComponentFactory
import org.gradle.api.internal.artifacts.ArtifactAttributes.ARTIFACT_FORMAT
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.*
import java.io.File
import java.util.*
import javax.inject.Inject

class InkrementalGenPlugin @Inject constructor(
    private val componentFactory: SoftwareComponentFactory
) : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        val extension = extensions.create<InkrementalMetaExtension>(EXTENSION).also { it.project = project }
        val android = extensions.findByType<LibraryExtension>()

        android ?: TODO("Only android library modules are supported at the moment")

        dependencies.registerTransform(AarToJarTransform::class.java) {
            from.attribute(ARTIFACT_FORMAT, FORMAT_AAR)
            to.attribute(ARTIFACT_FORMAT, ArtifactTypeDefinition.JAR_TYPE)
        }

        val flavorDimension = "$FLAVOR_DIMENSION.$name"

        android.flavorDimensions(flavorDimension)

        extension.androidModules.all {
            val (name, version) = this.name.split('#')
            val allNames = version.split('.')
                .scan("") { acc, it -> if (acc.isEmpty()) "-$it" else "$acc.$it" }
                .map { "$name$it" }
            val cfgName = allNames.last()
            val allSourceSets = (allNames + "gen-$cfgName")
                .map { "src/$it/kotlin" }
            (listOf<String?>(null) + allNames).windowed(2).forEach { (parent, current) ->
                createGenInputsConfiguration(current, version)
                    .extendsFrom(createGenInputsConfiguration(parent, version))
                createModuleInputsConfiguration(current)
                    .extendsFrom(createModuleInputsConfiguration(parent))
            }

            logger.debug("Flavor: $flavorDimension => $cfgName")

            with(android) {
                productFlavors {
                    register(cfgName) {
                        dimension = flavorDimension
                        // TODO move this hack out of here
                        if(name == "sdk") {
                            minSdkVersion(version)
                        }
                    }
                }
                sourceSets.named(cfgName) {
                    java.srcDirs(*allSourceSets.toTypedArray())
                }
            }
        }

        tasks.register<DefaultTask>(TASK_ALL_MODELS)
        tasks.register<DefaultTask>(TASK_ALL_DSLS)

        afterEvaluate {
            extension.modules.all {
                val module = this
                val name = module.name
                val platform = module.platform ?: error("Module $name does not declare platform")
                val type = module.type
                when(platform to type) {
                    InkrementalPlatform.ANDROID to InkrementalType.SDK ->
                        setupAndroidSdkModule(componentFactory, module)

                    InkrementalPlatform.ANDROID to InkrementalType.LIBRARY ->
                        setupAndroidLibraryModule(componentFactory, module)

                    else -> TODO("Module $name is unsupported (platform $platform, type $type)")
                }
            }
        }
    }
}

private const val TASK_ALL_MODELS = "generateModels"
private const val TASK_ALL_DSLS = "generateDsls"

const val EXTENSION = "inkremental"
const val CONFIGURATION_GEN = "inkrementalGen"
const val CONFIGURATION_MODULE = "inkremental"
const val CONFIGURATION_MODULE_DEF = "inkrementalDef"
const val FLAVOR_DIMENSION = "dev.inkremental.variant"
const val USAGE = "inkremental-meta"
const val FORMAT_AAR = "aar"

fun genConfigurationName(module: String?) = buildCamelCaseString(module, CONFIGURATION_GEN)
fun moduleConfigurationName(module: String?) = buildCamelCaseString(module, CONFIGURATION_MODULE)
fun moduleDefConfigurationName(module: String?) = buildCamelCaseString(module, CONFIGURATION_MODULE_DEF)

fun androidAarTaskName(name: String) = buildCamelCaseString("bundle", name, "ReleaseAar")
fun androidComponentName(name: String) = buildCamelCaseString(name, "Release")

fun loadPropertiesFromFile(file: File): Properties = Properties().apply {
    try {
        file.inputStream().use { load(it) }
    } catch(e: java.io.FileNotFoundException) {
        // do nothing
    }
}

fun Project.setupAndroidSdkModule(
    componentFactory: SoftwareComponentFactory,
    module: InkrementalMetaModule
) {
    val apiLevel = module.version.toInt()
    setupAndroidModule<GenerateAndroidSdkModelTask>(
        module,
        componentFactory,
        null
    ) {
        javadocContains = "It contains views and their setters for Android SDK (API level $apiLevel)"
        jarFiles = listOf(getAndroidJar(apiLevel))
        nullabilitySourceFiles = listOf(getAndroidJar(28))
    }
}

fun Project.setupAndroidLibraryModule(
    componentFactory: SoftwareComponentFactory,
    module: InkrementalMetaModule
) {
    val prefix = module.dslName

    val genInputs = configurations.getByName(genConfigurationName(prefix))
    val moduleInputs = configurations.getByName(moduleConfigurationName(prefix))

    configurations["${prefix}Api"].extendsFrom(genInputs)

    setupAndroidModule<GenerateAndroidLibraryModelTask>(
        module,
        componentFactory,
        moduleInputs
    ) {
        javadocContains = "It contains views and their setters for the library ${module.name}"
        configuration = genInputs
        sdkDependencies = listOf(getAndroidJar(28))
    }
}

private inline fun <reified T: GenerateModelTask> Project.setupAndroidModule(
    module: InkrementalMetaModule,
    componentFactory: SoftwareComponentFactory,
    modelInputs: Configuration? = null,
    noinline configuration: T.() -> Unit
) {
    val dslName = module.dslName
    val outputDir = getOutputDir(dslName)

    val outConfName = moduleDefConfigurationName(dslName)

    logger.debug("setupAndroidModule: $outConfName | ${outputDir.absolutePath}")

    val modelOutputs = configurations.create(outConfName) {
        description = "Descriptors of Inkremental module ${module.name}"
        isCanBeResolved = false
        isCanBeConsumed = true
        attributes {
            attribute(USAGE_ATTRIBUTE, objects.named(USAGE))
        }
    }

    val component = componentFactory.adhoc(buildCamelCaseString("inkrementalModel", dslName))
    components.add(component)
    component.addVariantsFromConfiguration(modelOutputs) {
        mapToMavenScope("inkremental")
    }

    val modelTask = tasks.register<T>(buildCamelCaseString("generate", dslName, "Model")) {
        quirks = module.quirks
        transformers = module.transformers
        camelCaseName = module.camelCaseName
        srcPackage = module.srcPackage
        modulePackage = module.modulePackage
        manualSetterName = module.manualSetterName
        outputFile = file(getModelOutputFile(module.dslName))
        configuration()
    }
    tasks.named(TASK_ALL_MODELS) {
        dependsOn(modelTask)
    }

    val modelFile = modelTask.get().outputFile
    artifacts.add(outConfName, modelFile) {
        builtBy(modelTask)
    }

    val dslTask = tasks.register<GenerateDslTask>(buildCamelCaseString("generate", dslName, "Dsl")) {
        dependsOn(modelTask)
        this.modelFile = modelFile
        this.configuration = modelInputs
        this.outputDir = outputDir
    }
    tasks.named(TASK_ALL_DSLS) {
        dependsOn(dslTask)
    }

    if(prop("dontGenerateCodeOnBuild") != "true") {
        listOf("Debug", "Release")
            .map { "compile${dslName.capitalize()}${it}Kotlin" }
            .forEach { tasks.named(it) { dependsOn(dslTask) } }
    }
}

private fun Project.createGenInputsConfiguration(prefix: String?, defVersion: String): Configuration =
    configurations.maybeCreate(genConfigurationName(prefix)) {
        description = "Input artifacts that should be processed by Inkremental code generator for module $name"
        isCanBeResolved = true
        isCanBeConsumed = false
        attributes {
            attribute(ARTIFACT_FORMAT, ArtifactTypeDefinition.JAR_TYPE) // JVM_CLASS_DIRECTORY
            //attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.CLASSES))
        }

        resolutionStrategy.eachDependency {
            if(requested.version.isNullOrEmpty()) {
                useVersion(defVersion)
                because("Using $prefix module version")
            }
        }
    }

private fun Project.createModuleInputsConfiguration(prefix: String? = null): Configuration =
    configurations.maybeCreate(moduleConfigurationName(prefix)) {
        description = "Input descriptors of Inkremental module $name"
        isCanBeResolved = true
        isCanBeConsumed = false
        attributes {
            attribute(USAGE_ATTRIBUTE, objects.named(USAGE))
        }
    }

private fun Project.getModelOutputFile(modelName: String) = buildDir / "inkremental" / "$modelName.json"
private fun Project.getOutputDir(sourceSetName: String) = projectDir / "src" / "gen-$sourceSetName" / "kotlin"

private fun Project.getAndroidJar(api: Int): File {
    val localProperties = File(rootDir, "local.properties")
    val sdkDir = loadPropertiesFromFile(localProperties).getProperty("sdk.dir")
        ?: System.getenv("ANDROID_SDK_ROOT")
        ?: System.getenv("ANDROID_HOME")
        ?: error("Android SDK location is not defined. " +
                "Please put SDK path to either local.properties file to property sdk.dir " +
                "or pass it via ANDROID_SDK_ROOT environment variable.")
    val jarFile = File("$sdkDir/platforms/android-$api/android.jar")
    if(!jarFile.exists()) error("Jar file for SDK $api is not found at ${jarFile.absolutePath}. " +
            "Please download platform $api via SDK manager or by invoking " +
            "the following command from shell: " +
            "sdkmanager \"platforms;android-$api\"")
    return jarFile
}
