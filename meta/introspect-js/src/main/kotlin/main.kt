package dev.inkremental.meta.introspect.js

import dev.inkremental.meta.introspect.descriptors.DescriptorIntrospector
import dev.inkremental.meta.model.InkrementalQuirks
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.config.AnalysisFlags.skipMetadataVersionCheck
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.descriptors.impl.ModuleDescriptorImpl
import org.jetbrains.kotlin.incremental.components.LookupTracker
import org.jetbrains.kotlin.js.resolve.JsPlatformAnalyzerServices.builtIns
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.CompilerDeserializationConfiguration
import org.jetbrains.kotlin.serialization.js.KotlinJavascriptSerializationUtil.readModuleAsProto
import org.jetbrains.kotlin.serialization.js.createKotlinJavascriptPackageFragmentProvider
import org.jetbrains.kotlin.storage.LockBasedStorageManager
import org.jetbrains.kotlin.utils.JsMetadataVersion
import org.jetbrains.kotlin.utils.KotlinJavascriptMetadataUtils
import java.io.File

fun main(args: Array<String>) {
    if(args.isEmpty()) {
        println("Usage: introspect kotlin-stdlib-js-dir")
        return
    }
    val distRoot = File(args[0])
    IosIntrospector(distRoot).provideViewModels(mutableMapOf())
}

class IosIntrospector(private val distRoot: File): DescriptorIntrospector() {
    override val rootViewFqName: FqName = FqName("org.w3c.dom.HTMLElement")

    override fun provideModelDescriptors(quirks: InkrementalQuirks): List<ModuleDescriptor> {
        val storageManager = LockBasedStorageManager("InkrementalStorage")
        val versionSpec = LanguageVersionSettingsImpl(LanguageVersion.KOTLIN_1_3, ApiVersion.KOTLIN_1_3)

        val fragments = KotlinJavascriptMetadataUtils.loadMetadata(distRoot)
        println("fragments [${fragments.size}]:")
        val modules = fragments.map { fragment ->
            println("\t${fragment.moduleName}")

            assert(fragment.version.isCompatible() || versionSpec.getFlag(skipMetadataVersionCheck)) {
                "Expected JS metadata version " +
                        JsMetadataVersion.INSTANCE +
                        ", but actual metadata version is " +
                        fragment.version
            }

            val moduleDescriptor = ModuleDescriptorImpl(
                Name.special("<" + fragment.moduleName + ">"),
                storageManager,
                builtIns
            )

            val lookupTracker = LookupTracker.DO_NOTHING
            val (header, body) = readModuleAsProto(fragment.body, fragment.version)
            val provider = createKotlinJavascriptPackageFragmentProvider(
                storageManager, moduleDescriptor, header, body, fragment.version,
                CompilerDeserializationConfiguration(versionSpec),
                lookupTracker
            )

            moduleDescriptor.initialize(provider)
            moduleDescriptor
        }

        modules.forEach { it.setDependencies(modules) }

        return modules
    }
}
