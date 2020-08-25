package dev.inkremental.meta.model

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.*
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule

val PoetModule: SerialModule = SerializersModule {
    // TODO set classDiscriminator to "kind" instead of default "type" when available:
    // https://github.com/Kotlin/kotlinx.serialization/issues/546
    polymorphic<TypeName> {
        ClassName::class with ClassNameSerializer
        ParameterizedTypeName::class with ParameterizedTypeNameSerializer
        LambdaTypeName::class with LambdaTypeNameSerializer
        TypeVariableName::class with TypeVariableNameSerializer
        WildcardTypeName::class with WildcardTypeNameSerializer
    }
}

val TypeNameSerializer: KSerializer<TypeName>
    get() {
        val t = requireNotNull(TypeName::class)
        val s = requireNotNull(PolymorphicSerializer(t))
        return s
    }
        //by lazy { PolymorphicSerializer(TypeName::class) }

private fun <T> SerialDescriptorBuilder.element(
    elementName: String,
    serializer: KSerializer<T>,
    annotations: List<Annotation> = emptyList(),
    isOptional: Boolean = false
) = element(elementName, serializer.descriptor, annotations, isOptional)

@Serializer(forClass = ClassName::class)
object ClassNameSerializer: KSerializer<ClassName> {
    override val descriptor: SerialDescriptor = SerialDescriptor("class") {
        element("canonicalName", String.serializer())
        element("isNullable", Boolean.serializer(), isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: ClassName) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.canonicalName)
            encodeBooleanElement(descriptor, 1, value.isNullable)
        }
    }

    override fun deserialize(decoder: Decoder): ClassName {
        var canonicalName: String? = null
        var isNullable: Boolean? = null
        var annotations: List<AnnotationSpec> = emptyList()

        decoder.decodeStructure(descriptor) {
            loop@ while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> canonicalName = decodeStringElement(descriptor, i)
                    1 -> isNullable = decodeBooleanElement(descriptor, i)
                    else -> throw SerializationException("Unknown index $i")
                }
            }
        }

        canonicalName ?: throw MissingFieldException("canonicalName")
        if (isNullable == null) isNullable = false
        return ClassName.bestGuess(canonicalName!!)
            .copy(nullable = isNullable!!, annotations = annotations)
    }
}

@Serializer(forClass = ParameterizedTypeName::class)
object ParameterizedTypeNameSerializer: KSerializer<ParameterizedTypeName> {
    override val descriptor: SerialDescriptor = SerialDescriptor("parameterized") {
        val s = requireNotNull(TypeNameSerializer)
        val t = s.list
        element("rawType", ClassNameSerializer)
        element("typeArguments", t)
        element("isNullable", Boolean.serializer(), isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: ParameterizedTypeName) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                0,
                ClassNameSerializer,
                value.rawType
            )
            encodeSerializableElement(
                descriptor,
                1,
                TypeNameSerializer.list,
                value.typeArguments
            )
            encodeBooleanElement(descriptor, 2, value.isNullable)
        }
    }

    override fun deserialize(decoder: Decoder): ParameterizedTypeName {
        var rawType: ClassName? = null
        var typeArguments: List<TypeName>? = null
        var isNullable: Boolean? = null
        var annotations: List<AnnotationSpec> = emptyList()

        decoder.decodeStructure(descriptor) {
            loop@ while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> rawType = decodeSerializableElement(
                        descriptor,
                        i,
                        ClassNameSerializer
                    )
                    1 -> typeArguments = decodeSerializableElement(
                        descriptor,
                        i,
                        TypeNameSerializer.list
                    )
                    2 -> isNullable = decodeBooleanElement(descriptor, i)
                    else -> throw SerializationException("Unknown index $i")
                }
            }
        }

        rawType ?: throw MissingFieldException("rawType")
        typeArguments ?: throw MissingFieldException("typeArguments")
        if (isNullable == null) isNullable = false
        return rawType!!.parameterizedBy(*typeArguments!!.toTypedArray())
            .copy(nullable = isNullable!!, annotations = annotations)
    }
}

@Serializer(forClass = LambdaTypeName::class)
object LambdaTypeNameSerializer: KSerializer<LambdaTypeName> {
    override val descriptor: SerialDescriptor = SerialDescriptor("lambda") {
        element("receiver", TypeNameSerializer, isOptional = true)
        element("parameters", ParameterSpecSerializer.list, isOptional = true)
        element("returnType", TypeNameSerializer)
        element("isNullable", Boolean.serializer(), isOptional = true)
        element("isSuspending", Boolean.serializer(), isOptional = true)
        //addElement("annotations", isOptional = true)
        // TODO implement AnnotationSpecSerializer
    }

    override fun serialize(encoder: Encoder, value: LambdaTypeName) {
        encoder.encodeStructure(descriptor) {
            value.receiver?.let {
                encodeSerializableElement(
                    descriptor,
                    0,
                    TypeNameSerializer,
                    it
                )
            }
            if(value.parameters.isNotEmpty()) {
                encodeSerializableElement(
                    descriptor,
                    1,
                    ParameterSpecSerializer.list,
                    value.parameters
                )
            }
            encodeSerializableElement(
                descriptor,
                2,
                TypeNameSerializer,
                value.returnType
            )
            encodeBooleanElement(
                descriptor,
                3,
                value.isNullable
            )
            encodeBooleanElement(
                descriptor,
                4,
                value.isSuspending
            )
        }
    }

    override fun deserialize(decoder: Decoder): LambdaTypeName {
        var receiver: TypeName? = null
        var parameters: List<ParameterSpec> = emptyList()
        var returnType: TypeName? = null
        var isNullable: Boolean? = null
        var isSuspending: Boolean? = null
        var annotations: List<AnnotationSpec> = emptyList()

        decoder.decodeStructure(descriptor) {
            loop@ while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> receiver = decodeSerializableElement(
                        descriptor,
                        i,
                        TypeNameSerializer
                    )
                    1 -> parameters = decodeSerializableElement(
                        descriptor,
                        i,
                        ParameterSpecSerializer.list
                    )
                    2 -> returnType = decodeSerializableElement(
                        descriptor,
                        i,
                        TypeNameSerializer
                    )
                    3 -> isNullable = decodeBooleanElement(descriptor, i)
                    4 -> isSuspending = decodeBooleanElement(descriptor, i)
                    else -> throw SerializationException("Unknown index $i")
                }
            }
        }

        returnType ?: throw MissingFieldException("returnType")
        if (isNullable == null) isNullable = false
        if (isSuspending == null) isSuspending = false
        return LambdaTypeName
            .get(receiver, parameters, returnType!!)
            .copy(isNullable!!, annotations, isSuspending!!)
    }
}

@Serializer(forClass = ParameterSpec::class)
object ParameterSpecSerializer: KSerializer<ParameterSpec> {
    override val descriptor: SerialDescriptor = SerialDescriptor("parameter") {
        element("name", String.serializer())
        element("type", TypeNameSerializer)
    }

    override fun serialize(encoder: Encoder, value: ParameterSpec) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(
                descriptor,
                0,
                value.name
            )
            encodeSerializableElement(
                descriptor,
                1,
                TypeNameSerializer,
                value.type
            )
        }
    }

    override fun deserialize(decoder: Decoder): ParameterSpec {
        var name: String? = null
        var type: TypeName? = null

        decoder.decodeStructure(descriptor) {
            loop@ while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> name = decodeStringElement(descriptor, i)
                    1 -> type = decodeSerializableElement(
                        descriptor,
                        i,
                        TypeNameSerializer
                    )
                    else -> throw SerializationException("Unknown index $i")
                }
            }
        }

        name ?: throw MissingFieldException("name")
        type ?: throw MissingFieldException("type")
        return ParameterSpec.builder(name!!, type!!).build()
    }
}

@Serializer(forClass = TypeVariableName::class)
object TypeVariableNameSerializer: KSerializer<TypeVariableName> {
    override val descriptor: SerialDescriptor = SerialDescriptor("typeVar") {
        element("name", String.serializer())
        element("bounds", TypeNameSerializer.list, isOptional = true)
        element("variance", String.serializer(), isOptional = true)
        element("isReified", Boolean.serializer())
        element("isNullable", Boolean.serializer(), isOptional = true)
        //addElement("annotations", isOptional = true)
        // TODO implement AnnotationSpecSerializer
    }

    override fun serialize(encoder: Encoder, value: TypeVariableName) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(
                descriptor,
                0,
                value.name
            )
            if(value.bounds.isNotEmpty()) {
                encodeSerializableElement(
                    descriptor,
                    1,
                    TypeNameSerializer.list,
                    value.bounds
                )
            }
            value.variance?.let {
                encodeStringElement(
                    descriptor,
                    2,
                    it.name
                )
            }
            encodeBooleanElement(
                descriptor,
                3,
                value.isReified
            )
            encodeBooleanElement(
                descriptor,
                4,
                value.isNullable
            )
        }
    }

    override fun deserialize(decoder: Decoder): TypeVariableName {
        var name: String? = null
        var bounds: List<TypeName> = emptyList()

        /** Either [KModifier.IN], [KModifier.OUT], or null. */
        var variance: KModifier? = null
        var isReified: Boolean? = null
        var isNullable: Boolean? = null
        var annotations: List<AnnotationSpec> = emptyList()

        decoder.decodeStructure(descriptor) {
            loop@ while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> name = decodeStringElement(descriptor, i)
                    1 -> bounds = decodeSerializableElement(
                        descriptor,
                        i,
                        TypeNameSerializer.list
                    )
                    2 -> variance = KModifier.valueOf(decodeStringElement(descriptor, i))
                    3 -> isReified = decodeBooleanElement(descriptor, i)
                    4 -> isNullable = decodeBooleanElement(descriptor, i)
                    else -> throw SerializationException("Unknown index $i")
                }
            }
        }

        name ?: throw MissingFieldException("name")
        isReified ?: throw MissingFieldException("isReified")
        if (isReified == null) isReified = false
        if (isNullable == null) isNullable = false
        return TypeVariableName(name = name!!, bounds = *bounds.toTypedArray(), variance = variance)
            .copy(isNullable!!, annotations, bounds, isReified!!)
    }
}

@Serializer(forClass = WildcardTypeName::class)
object WildcardTypeNameSerializer: KSerializer<WildcardTypeName> {
    override val descriptor: SerialDescriptor = SerialDescriptor("wildcard") {
        element("outTypes", TypeNameSerializer.list, isOptional = true)
        element("inTypes", TypeNameSerializer.list, isOptional = true)
        element("isNullable", Boolean.serializer(), isOptional = true)
        //addElement("annotations", isOptional = true)
        // TODO implement AnnotationSpecSerializer
    }

    override fun serialize(encoder: Encoder, value: WildcardTypeName) {
        encoder.encodeStructure(descriptor) {
            if(value.outTypes.isNotEmpty()) {
                encodeSerializableElement(
                    descriptor,
                    0,
                    TypeNameSerializer.list,
                    value.outTypes
                )
            }
            if(value.inTypes.isNotEmpty()) {
                encodeSerializableElement(
                    descriptor,
                    1,
                    TypeNameSerializer.list,
                    value.inTypes
                )
            }
            encodeBooleanElement(
                descriptor,
                2,
                value.isNullable
            )
            endStructure(descriptor)
        }
    }

    override fun deserialize(decoder: Decoder): WildcardTypeName {
        var outTypes: List<TypeName> = emptyList()
        var inTypes: List<TypeName> = emptyList()
        var isNullable: Boolean? = null
        var annotations: List<AnnotationSpec> = emptyList()

        decoder.decodeStructure(descriptor) {
            loop@ while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> outTypes = decodeSerializableElement(
                        descriptor,
                        i,
                        TypeNameSerializer.list
                    )
                    1 -> inTypes = decodeSerializableElement(
                        descriptor,
                        i,
                        TypeNameSerializer.list
                    )
                    2 -> isNullable = decodeBooleanElement(descriptor, i)
                    else -> throw SerializationException("Unknown index $i")
                }
            }
        }

        if (isNullable == null) isNullable = false
        return when {
            outTypes.size != 1 -> throw SerializationException("outTypes should have exactly one element")
            inTypes.isEmpty() -> WildcardTypeName.producerOf(outTypes[0])
            inTypes.size != 1 -> throw SerializationException("inTypes should have zero or one element")
            else -> WildcardTypeName.consumerOf(inTypes[0])
        }.copy(isNullable!!, annotations)
    }
}

@Serializer(forClass = MemberName::class)
object MemberNameSerializer: KSerializer<MemberName> {
    override val descriptor: SerialDescriptor = SerialDescriptor("member") {
        element("packageName", String.serializer(), isOptional = true)
        element("enclosingClassName", ClassNameSerializer, isOptional = true)
        element("simpleName", String.serializer())
    }

    override fun serialize(encoder: Encoder, value: MemberName) {
        encoder.encodeStructure(descriptor) {
            if(value.enclosingClassName != null && value.packageName.isNotEmpty()) {
                TODO("Both packageName and enclosingClassName are present")
            }
            if(value.packageName.isNotEmpty()) {
                encodeStringElement(descriptor, 0, value.packageName)
            }
            value.enclosingClassName?.let {
                encodeSerializableElement(
                    descriptor,
                    1,
                    ClassNameSerializer,
                    it
                )
            }
            encodeStringElement(descriptor, 2, value.simpleName)
        }
    }

    override fun deserialize(decoder: Decoder): MemberName {
        var packageName: String? = null
        var enclosingClassName: ClassName? = null
        var simpleName: String? = null

        decoder.decodeStructure(descriptor) {
            loop@ while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    0 -> packageName = decodeStringElement(descriptor, i)
                    1 -> enclosingClassName = decodeSerializableElement(
                        descriptor,
                        i,
                        ClassNameSerializer
                    )
                    2 -> simpleName = decodeStringElement(descriptor, i)
                    else -> throw SerializationException("Unknown index $i")
                }
            }
        }

        simpleName ?: throw MissingFieldException("simpleName")
        return when {
            packageName != null -> MemberName(packageName!!, simpleName!!)
            enclosingClassName != null -> MemberName(enclosingClassName!!, simpleName!!)
            else -> throw SerializationException("Either packageName or enclosingClassName should be present")
        }
    }
}
