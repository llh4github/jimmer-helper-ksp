package com.github.llh4github.jimmerhelper.core.generator

import com.github.llh4github.jimmerhelper.core.common.JimmerMember
import com.github.llh4github.jimmerhelper.core.dto.ClassInfoDto
import com.github.llh4github.jimmerhelper.core.dto.FieldInfoDto
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

/**
 *
 * Created At 2023/6/9 10:43
 * @author llh
 */
class FetcherHelperGen(private val dto: ClassInfoDto) {
    private val fetchDsl = ClassName(dto.packageName, "${dto.className}FetcherDsl")
    private fun fetcherReturnType(dto: ClassInfoDto): ParameterizedTypeName {
        return JimmerMember.fetcherClass.parameterizedBy(ClassName(dto.packageName, dto.className))
    }

    fun build(): TypeSpec {
        val builder = TypeSpec.objectBuilder("${dto.className}SimpleFetcher")
            .addKdoc(comment)
            .addAnnotation(suppressWarns)

        builder.addProperty(allScalarFieldsProperty)
        builder.addProperty(allTableFieldsProperty)
        allKeyProperties(dto.fields)?.let { builder.addProperty(it) }
        return builder.build()
    }

    private val allScalarFieldsProperty = PropertySpec.builder("allScalarFields", fetcherReturnType(dto))
        .initializer("%M(%L::class).by{%L}", JimmerMember.newFetcherFun, dto.className, "allScalarFields()")
        .build()
    private val allTableFieldsProperty = PropertySpec.builder("allTableFields", fetcherReturnType(dto))
        .initializer("%M(%L::class).by{%L}", JimmerMember.newFetcherFun, dto.className, "allTableFields()")
        .build()

    private fun allKeyProperties(fields: List<FieldInfoDto>): PropertySpec? {
        val list = fields.filter { it.isJimmerKey }.toList()
        if (list.isEmpty()) {
            return null
        }
        val builder = PropertySpec.builder("allKeyProperties", fetcherReturnType(dto))
        val cb = CodeBlock.builder()
        cb.addStatement("%M(%L::class).by{", JimmerMember.newFetcherFun, dto.className)
        list.forEach {
            cb.addStatement("%L()", it.name)
        }
        cb.addStatement("}")
        builder.initializer(cb.build())
        return builder.build()
    }

}