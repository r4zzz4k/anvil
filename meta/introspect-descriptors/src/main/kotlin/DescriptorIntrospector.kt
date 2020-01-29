package dev.inkremental.meta.introspect.descriptors

import dev.inkremental.meta.model.*
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.name.FqName

abstract class DescriptorIntrospector: Introspector {
    abstract val rootViewFqName: FqName
    abstract fun provideModelDescriptors(quirks: InkrementalQuirks): List<ModuleDescriptor>

    override fun provideViewModels(quirks: InkrementalQuirks): List<ViewModel> {
        val descriptors = provideModelDescriptors(quirks)

        val models = mutableListOf<MutableViewModel>()

        descriptors.forEach { it.accept(IntrospectVisitor(rootViewFqName, models), Unit) }

        return models.map(MutableViewModel::build)
    }
}
