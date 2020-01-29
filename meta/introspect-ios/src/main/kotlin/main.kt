package dev.inkremental.meta.introspect.ios

import dev.inkremental.meta.introspect.descriptors.DescriptorIntrospector
import dev.inkremental.meta.model.InkrementalQuirks
import dev.inkremental.meta.model.div
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.konan.library.KonanFactories
import org.jetbrains.kotlin.konan.library.konanPlatformLibraryPath
import org.jetbrains.kotlin.konan.target.buildDistribution
import org.jetbrains.kotlin.library.impl.createKotlinLibrary
import org.jetbrains.kotlin.library.resolverByName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.storage.LockBasedStorageManager
import org.jetbrains.kotlin.util.Logger
import java.io.File
import org.jetbrains.kotlin.konan.file.File as KFile

fun main(args: Array<String>) {
    if(args.isEmpty()) {
        println("Usage: introspect kotlin-native-dist-dir")
        return
    }
    val distRoot = File(args[0])
    IosIntrospector(distRoot).provideViewModels(mutableMapOf())
}

class IosIntrospector(private val distRoot: File): DescriptorIntrospector() {
    override val rootViewFqName: FqName = FqName("platform.UIKit.UIView")

    override fun provideModelDescriptors(quirks: InkrementalQuirks): List<ModuleDescriptor> {
        val distribution = buildDistribution(distRoot.absolutePath)
        println("Home: ${distribution.konanHome}")
        println("klib: ${distribution.klib}")

        val logger = SimpleLogger()
        val resolver = resolverByName(
            emptyList(),
            distributionKlib = distribution.klib,
            skipCurrentDir = true,
            logger = logger)

        val library = createKotlinLibrary(
            KFile(distRoot / konanPlatformLibraryPath("UIKit", "ios_arm64")))

        val storageManager = LockBasedStorageManager("InkrementalStorage")
        val versionSpec = LanguageVersionSettingsImpl(LanguageVersion.KOTLIN_1_3, ApiVersion.KOTLIN_1_3)

        val moduleDescriptor = KonanFactories.DefaultDeserializedDescriptorFactory.createDescriptorAndNewBuiltIns(
            library, versionSpec, storageManager, null)

        val defaultModules = resolver.defaultLinks(noStdLib = false, noDefaultLibs = true, noEndorsedLibs = true)
            .map {
                println("Default module: ${it.libraryName}")
                KonanFactories.DefaultDeserializedDescriptorFactory.createDescriptor(
                    it, versionSpec, storageManager, moduleDescriptor.builtIns, null)
            }

        (defaultModules + moduleDescriptor).let { allModules ->
            allModules.forEach { it.setDependencies(allModules) }
        }

        println("Library name: ${library.libraryName}, files: ${library.fileCount()}")

        return listOf(moduleDescriptor)
    }
}

private fun KFile(input: File): KFile = KFile(input.absolutePath)

class SimpleLogger(
    private val tag: String = "deserializer"
) : Logger {
    private fun log(level: String, message: String) = println("$level: [$tag] $message")
    override fun log(message: String) = log("v", message)
    override fun warning(message: String) = log("w", message)
    override fun error(message: String) = log("e", message)
    override fun fatal(message: String): Nothing {
        log("v", message)
        kotlin.error(message)
    }
}
