package dev.inkremental.meta.model

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals

class PoetSerializerTest {
    private val json = JsonConfiguration(
        false,
        prettyPrint = false,
        serialModule = PoetModule,
    )

    @Test fun className() {
        val expected =
            """{""" +
            """"type":"class",""" +
            """"canonicalName":"dev.inkremental.meta.model.PoetSerializerTest"""" +
            """}"""
        val input = PoetSerializerTest::class.asClassName()
        val serialized = json.toJson(TypeNameSerializer, input)
        val actual = serialized.toString()
        println(actual)
        assertEquals(expected, actual)
        val output = json.fromJson(TypeNameSerializer, serialized)
        assertEquals(input, output)
    }

    @Test fun parametrizedName() {
        val expected =
            """{""" +
            """"type":"parameterized",""" +
            """"rawType":{"canonicalName":"dev.inkremental.meta.model.PoetSerializerTest"},""" +
            """"typeArguments":[{"type":"class","canonicalName":"kotlin.Int"}]""" +
            """}"""
        val input = PoetSerializerTest::class.asClassName().parameterizedBy(Int::class.asTypeName())
        val serialized = json.toJson(TypeNameSerializer, input)
        val actual = serialized.toString()
        assertEquals(expected, actual)
        val output = json.fromJson(TypeNameSerializer, serialized)
        assertEquals(input, output)
    }
}
