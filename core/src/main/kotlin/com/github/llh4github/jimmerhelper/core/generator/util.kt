package com.github.llh4github.jimmerhelper.core.generator

import com.github.llh4github.jimmerhelper.core.common.inputDtoSuffix
import com.github.llh4github.jimmerhelper.core.dto.ClassInfoDto
import com.github.llh4github.jimmerhelper.core.dto.FieldInfoDto
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

internal fun dataClassBuilder(dto: ClassInfoDto) =
    TypeSpec.classBuilder(dto.inputDtoClassName)
        .addModifiers(KModifier.DATA)

internal fun interfaceBuilder(dto: ClassInfoDto) =
    TypeSpec.interfaceBuilder(dto.inputDtoClassName)
        .addModifiers(KModifier.PUBLIC)

internal fun properties(fields: List<FieldInfoDto>, init: Boolean = false): List<PropertySpec> {
    return fields
        .filter { !it.isIdViewListField } // 暂不处理此类型字段
        .map {
        val type = propertyType(it)
        val propertySpec = if (it.isList) {
            PropertySpec.builder(it.name, type)
                .mutable(true)
        } else {
            PropertySpec.builder(it.name, type)
                .mutable(true)
        }
        if (init) {
            return@map propertySpec.initializer(propertyDefaultValue(it)).build()
        } else {
            return@map propertySpec.build()
        }
    }.toList()
}

internal fun propertyType(field: FieldInfoDto): TypeName {
    return if (field.isRelationField) {
        if (field.isList)
            ClassName(field.typePackage, field.typeName)
                .parameterizedBy(ClassName(field.typeParamTypeInputDtoPkgName!!, field.typeParamTypeInputDtoName!!))
        else ClassName(field.typePackage, field.typeName + inputDtoSuffix).copy(true)
    } else {
        ClassName(field.typePackage, field.typeName).copy(true)
    }
}

internal fun propertyDefaultValue(field: FieldInfoDto): String {
    return if (field.isList) {
        "emptyList()"
    } else {
        "null"
    }
}