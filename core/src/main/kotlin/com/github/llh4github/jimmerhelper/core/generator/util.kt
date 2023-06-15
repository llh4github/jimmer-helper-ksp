package com.github.llh4github.jimmerhelper.core.generator

import com.github.llh4github.jimmerhelper.core.common.JimmerMember
import com.github.llh4github.jimmerhelper.core.common.SUPER_CLASS_MAP
import com.github.llh4github.jimmerhelper.core.common.inputDtoPkgName
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

/**
 * [fieldName]是否是父接口中的字段。
 * [parentName]：当前类的父接口名
 */
internal fun isParentField(parentName: List<String>, fieldName: String): Boolean {
    if (parentName.isEmpty()) {
        return false
    }
    return SUPER_CLASS_MAP.filter { parentName.contains(it.key) }
        .flatMap { it.value.fields }
        .map { it.name }
        .any { it == fieldName }
}

/**
 * 生成类的提醒注释
 */
internal val comment = CodeBlock.builder()
    .add("由 %L 插件生成。请勿修改。\n", "jimmer-helper-ksp")
    .build()

internal val suppressWarns = AnnotationSpec.builder(Suppress::class)
    .apply {
        addMember("\"RedundantVisibilityModifier\"")
        addMember("\"Unused\"")
    }
    .build()
internal fun inputInterface(dto: ClassInfoDto): ParameterizedTypeName {
    return JimmerMember.inputInterface.parameterizedBy(ClassName(dto.packageName, dto.className))
}

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
        if (field.isList) {
            val clazz = ClassName(field.typePackage, field.typeName)
                .parameterizedBy(ClassName(field.typeParamTypeInputDtoPkgName!!, field.typeParamTypeInputDtoName!!))
//            logger.info("type argument : ${clazz.typeArguments}")
            clazz
        } else {
            ClassName(field.typePackage + ".${inputDtoPkgName}", field.typeName + inputDtoSuffix).copy(true)
        }
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