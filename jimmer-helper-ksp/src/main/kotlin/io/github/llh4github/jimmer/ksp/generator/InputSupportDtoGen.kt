package io.github.llh4github.jimmer.ksp.generator

import com.squareup.kotlinpoet.*
import io.github.llh4github.jimmer.ksp.dto.ClassDefinition

/**
 * input-support-dto类生成逻辑
 *
 * Created At 2023/9/12 11:53
 * @author llh
 */
object InputSupportDtoGen : AbstractGenerator() {
    private lateinit var allClass: List<ClassDefinition>
    override fun generate(list: List<ClassDefinition>): List<FileSpec> {
        allClass = list
        return list
            .filter { it.isJimmerModel }
            .filter { !it.isMappedSuperclass }
            .map { generateInputSupportDto(it) }
            .toList()
    }

    private fun generateInputSupportDto(definition: ClassDefinition): FileSpec {
        val fileBuilder = FileSpec.builder(definition.inputDtoPkg, definition.inputDtoClassName)
            .addImport(definition.packageName, definition.name)
        fileBuilder.addImport(definition.packageName, "by")
        val constructorProperties = mutableListOf<PropertySpec>()
        val constructFun = constructorFun(definition, constructorProperties)
        return fileBuilder.addType(
            TypeSpec.classBuilder(definition.inputDtoClassName)
                .addKdoc(comment)
                .addModifiers(KModifier.DATA)
                .primaryConstructor(constructFun)
                .addProperties(constructorProperties)
                .addAnnotation(suppressWarns)
                .build()
        ).build()
    }

    private fun constructorFun(
        definition: ClassDefinition, constructorProperties: MutableList<PropertySpec>
    ): FunSpec {
        val constructorFun = FunSpec.constructorBuilder()
        definition.fields
            .filter { !it.jimmerFieldRestrict.isIdViewListField }
            .forEach {
                val type = it.propertyType()
                val defaultValue = it.propertyDefaultValueStr()
                val propertySpec = PropertySpec.builder(it.name, type)
                    .mutable(!it.isListField)
                    .initializer(it.name)
                if (isParentField(definition.parentNames, it.name)) {
                    propertySpec.addModifiers(KModifier.OVERRIDE)
                }
                constructorProperties.add(propertySpec.build())
                constructorFun.addParameter(
                    ParameterSpec.builder(it.name, type)
                        .defaultValue(defaultValue)
                        .build()
                )
            }
        return constructorFun.build()
    }

    private fun isParentField(parentName: List<String>, fieldName: String): Boolean {
        if (parentName.isEmpty()) {
            return false
        }
        return allClass.filter { it.isMappedSuperclass }
            .filter { parentName.contains(it.name) }
            .flatMap { it.fields }
            .map { it.name }
            .any { it == fieldName }
    }


}