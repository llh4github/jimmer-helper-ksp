package com.github.llh4github.jimmerhelper.core.generator

import com.github.llh4github.jimmerhelper.core.common.JimmerMember
import com.github.llh4github.jimmerhelper.core.dto.ClassInfoDto
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

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

        return builder.build()
    }

    private val allScalarFieldsProperty = PropertySpec.builder("allScalarFields", fetcherReturnType(dto))
        .initializer("%M(%L::class).by{%L}", JimmerMember.newFetcherFun, dto.className, "allScalarFields()")
        .build()
    private val allTableFieldsProperty = PropertySpec.builder("allTableFields", fetcherReturnType(dto))
        .initializer("%M(%L::class).by{%L}", JimmerMember.newFetcherFun, dto.className, "allTableFields()")
        .build()

}