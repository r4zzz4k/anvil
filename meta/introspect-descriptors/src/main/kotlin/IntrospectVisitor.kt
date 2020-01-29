package dev.inkremental.meta.introspect.descriptors

import com.squareup.kotlinpoet.ClassName
import dev.inkremental.meta.model.MutableViewModel
import dev.inkremental.meta.model.ViewModelSupertype
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.impl.DeclarationDescriptorVisitorEmptyBodies
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.descriptorUtil.*

class IntrospectVisitor(
    private val rootFqName: FqName,
    private val models: MutableList<MutableViewModel>
) : DeclarationDescriptorVisitorEmptyBodies<Unit, Unit>() {
    override fun visitModuleDeclaration(descriptor: ModuleDescriptor, data: Unit) {
        val fragments = descriptor.getPackageFragments()
        println("Module: ${descriptor.name}, fragments: ${fragments.size}")
        fragments.forEach { it.accept(this, data) }
    }

    override fun visitPackageFragmentDescriptor(descriptor: PackageFragmentDescriptor, data: Unit) {
        val children = descriptor.getMemberScope().getContributedDescriptors()
        if (children.isNotEmpty()) {
            val packageName = descriptor.fqName.asString()
            //if(!packageName.startsWith("org.w3c.dom")) return
            //println("Package $packageName")
            children.forEach { it.accept(this, data) }
        }
    }

    private var viewModel: MutableViewModel? = null

    override fun visitClassDescriptor(descriptor: ClassDescriptor, data: Unit) {
        val superTypes = descriptor.getAllSuperclassesWithoutAny()
        val isViewClass = superTypes.any { it.fqNameSafe == rootFqName }
        /*val isViewClass = superTypes.any { it.fqNameSafe == FqName("org.w3c.dom.Element") } &&
            superTypes.none { it.fqNameSafe == FqName("org.w3c.dom.HTMLElement") } &&
            superTypes.none { it.fqNameSafe == FqName("org.w3c.dom.svg.SVGElement") }*/
        if(isViewClass) {
            val view = MutableViewModel()
            view.name = descriptor.name.asString()
            view.plainType = ClassName.bestGuess(descriptor.fqNameSafe.asString())
            viewModel = view
            println("Class: ${descriptor.name.asString()} | ${descriptor.kind.name}")
            val children = descriptor.unsubstitutedMemberScope.getContributedDescriptors()
            val constructors = descriptor.constructors
            if (children.isNotEmpty() || constructors.isNotEmpty()) {
                println("\tsuper:")
                view.superType = ViewModelSupertype.Unresolved(
                    superTypes
                        .map { it.fqNameSafe.asString() }
                        .onEach { println("\t\t$it") }
                )
                println("\tctors:")
                constructors.forEach { it.accept(this, data) }
                println("\tchildren:")
                children.forEach { it.accept(this, data) }
            }
            models += view
        }
    }

    override fun visitFunctionDescriptor(descriptor: FunctionDescriptor, data: Unit) {
        val viewModel = viewModel ?: return
        //val container = descriptor.containingDeclaration
        if(descriptor.kind != CallableMemberDescriptor.Kind.FAKE_OVERRIDE) {
            if(descriptor.name.asString().startsWith("set")) {
                println("\t\tfun: ${descriptor.name} | ${descriptor.kind}")
            }
        }
    }
}

private fun getPackagesFqNames(module: ModuleDescriptor): Set<FqName> {
    val result = mutableSetOf<FqName>()

    fun getSubPackages(fqName: FqName) {
        result.add(fqName)
        module.getSubPackagesOf(fqName) { true }.forEach { getSubPackages(it) }
    }

    getSubPackages(FqName.ROOT)
    return result
}

private fun ModuleDescriptor.getPackageFragments(): List<PackageFragmentDescriptor> =
    getPackagesFqNames(this).flatMap {
        getPackage(it).fragments.filter { it.module == this }
    }
