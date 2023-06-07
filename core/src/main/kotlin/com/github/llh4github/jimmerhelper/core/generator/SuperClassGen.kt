package com.github.llh4github.jimmerhelper.core.generator

import com.github.llh4github.jimmerhelper.core.common.inputDtoSuffix
import com.github.llh4github.jimmerhelper.core.dto.ClassInfoDto
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javaslang.Tuple2

class SuperClassGen(private val dto: ClassInfoDto) {
    private val typeSpec = TypeSpec.classBuilder(dto.inputDtoClassName)
        .addModifiers(KModifier.DATA)

    fun build(): FileSpec {

        val tuple = constructorFun(dto)
        return FileSpec.builder(dto.inputDtoPkg, dto.inputDtoClassName)
            .addImport(dto.packageName, dto.className)
            .addType(
                typeSpec
                    .addAnnotation(
                        AnnotationSpec.builder(Suppress::class)
                            .apply {
                                addMember("\"RedundantVisibilityModifier\"")
                                addMember("\"Unused\"")
                            }
                            .build()

                    )
                    .addProperties(tuple._2)
                    .primaryConstructor(tuple._1)
                    .build()

            ).build()
    }

    private fun constructorFun(dto: ClassInfoDto): Tuple2<FunSpec, MutableList<PropertySpec>> {
        val constructorFun = FunSpec.constructorBuilder()
        val propertyList = mutableListOf<PropertySpec>()
        dto.fields.forEach {
            val type = if (it.isRelationField) {
                if (it.isList)
                    ClassName(it.typePackage, it.typeName)
                        .parameterizedBy(ClassName(it.typeParamPkgStr!!, it.typeParamTypeSupportName!!))
                else ClassName(it.typePackage, it.typeName + inputDtoSuffix).copy(true)
            } else {
                ClassName(it.typePackage, it.typeName).copy(true)
            }
            val defaultValue = if (it.isList) {
                "emptyList()"
            } else {
                "null"
            }
            val propertySpec = if (it.isList) {
                PropertySpec.builder(it.name, type)
                    .mutable(true)
                    .initializer(it.name)
                    .build()
            } else {
                PropertySpec.builder(it.name, type)
                    .mutable(true)
                    .initializer(it.name)
                    .build()
            }

            propertyList.add(propertySpec)
            constructorFun.addParameter(
                ParameterSpec.builder(it.name, type)
                    .defaultValue(defaultValue)
                    .build()
            )

        }
        val primaryConstructor = constructorFun.build()
        return Tuple2(primaryConstructor, propertyList)
    }
}