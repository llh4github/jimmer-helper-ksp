package com.github.llh4github.jimmerhelper.core.generator

import com.github.llh4github.jimmerhelper.core.common.JimmerMember
import com.github.llh4github.jimmerhelper.core.common.SUPER_CLASS_MAP
import com.github.llh4github.jimmerhelper.core.dto.ClassInfoDto
import com.github.llh4github.jimmerhelper.core.dto.FieldInfoDto
import com.squareup.kotlinpoet.*
import javaslang.Tuple2

class InputClassGen(private val dto: ClassInfoDto) {
    private val typeSpec = dataClassBuilder(dto)

    fun build(): FileSpec {

        val tuple = constructorFun(dto)
        val builder = FileSpec.builder(dto.inputDtoPkg, dto.inputDtoClassName)
            .addImport(dto.packageName, dto.className)
        builder.addImport(dto.packageName, needImportJimmerExtFun(dto))


        return builder
            .addType(
                typeSpec
                    .addKdoc(comment)
                    .addSuperinterface(inputInterface(dto))
                    .addSuperinterfaces(inputParentInterface(dto))
                    .addFunction(overrideToEntity(dto))
                    .addAnnotation(suppressWarns)
                    .addProperties(tuple._2)
                    .primaryConstructor(tuple._1)
                    .build()

            )
            .addType(FetcherHelperGen(dto).build())
            .build()
    }


    private fun inputParentInterface(dto: ClassInfoDto): List<ClassName> {
        return dto.parentNames
            .filter { SUPER_CLASS_MAP.containsKey(it) }
            .map {
                val p = SUPER_CLASS_MAP[it]!!
                ClassName(p.inputDtoPkg, p.inputDtoClassName)
            }.toList()
    }

    private fun constructorFun(dto: ClassInfoDto): Tuple2<FunSpec, MutableList<PropertySpec>> {
        val constructorFun = FunSpec.constructorBuilder()
        val propertyList = mutableListOf<PropertySpec>()
        val parentNames = dto.parentNames
        dto.fields
            .filter { !it.isIdViewListField }
            .forEach {
                val type = propertyType(it)
                val defaultValue = propertyDefaultValue(it)

                val propertySpec = if (it.isList) {
                    PropertySpec.builder(it.name, type)
                        .mutable(false)
                        .initializer(it.name)
                } else {
                    PropertySpec.builder(it.name, type)
                        .mutable(true)
                        .initializer(it.name)
                }
                if (isParentField(parentNames, it.name)) {
                    propertySpec.addModifiers(KModifier.OVERRIDE)
                }

                propertyList.add(propertySpec.build())
                constructorFun.addParameter(
                    ParameterSpec.builder(it.name, type)
                        .defaultValue(defaultValue)
                        .build()
                )

            }
        val primaryConstructor = constructorFun.build()
        return Tuple2(primaryConstructor, propertyList)
    }

    private fun needImportJimmerExtFun(dto: ClassInfoDto): List<String> {
        val hasList = dto.fields.any { it.isList }
        if (!hasList) {
            return listOf("by")
        }
        return listOf("by", "addBy")

    }

    private fun overrideToEntity(dto: ClassInfoDto): FunSpec {
        val builder = FunSpec.builder("toEntity")
            .addModifiers(KModifier.OVERRIDE)
            .returns(ClassName(dto.packageName, dto.className))
            .addCode("return ")
            .addStatement("%M(%L::class).by{", JimmerMember.newFun, dto.className)
        dto.fields.forEach {
            if (it.isIdViewListField) {
                return@forEach
            }
            if (it.isRelationField) {
                relationFields(builder, it, dto)
                return@forEach
            }
            builder
                .addStatement("this@%L.%L?.let{", dto.inputDtoClassName, it.name)
                .addStatement("%L = it", it.name)
                .addStatement("}")
        }
        return builder.addStatement("}").build()
    }

    private fun relationFields(builder: FunSpec.Builder, field: FieldInfoDto, dto: ClassInfoDto) {
        if (field.isList) {
            builder
                .addStatement("this@%L.%L.forEach{", dto.inputDtoClassName, field.name)
                .addStatement("%L().addBy{ it.toEntity() }", field.name)
                .addStatement("}")
        } else {

            builder
                .addStatement("this@%L.%L?.let{", dto.inputDtoClassName, field.name)
                .addStatement("%L = it.toEntity()", field.name)
                .addStatement("}")
        }
    }
}