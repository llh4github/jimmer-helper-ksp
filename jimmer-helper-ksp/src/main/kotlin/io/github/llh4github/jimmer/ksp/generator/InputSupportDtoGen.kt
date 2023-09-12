package io.github.llh4github.jimmer.ksp.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.github.llh4github.jimmer.ksp.common.JimmerMember
import io.github.llh4github.jimmer.ksp.dto.ClassDefinition
import io.github.llh4github.jimmer.ksp.dto.FieldDefinition

/**
 * input-support-dto类生成逻辑
 *
 * Created At 2023/9/12 11:53
 * @author llh
 */
object InputSupportDtoGen : AbstractGenerator() {
    private lateinit var allClass: List<ClassDefinition>
    private val superClass: List<ClassDefinition> by lazy {
        allClass.filter { it.isMappedSuperclass }.toList()
    }

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
                .addSuperinterface(jimmerInputInterface(definition))
                .addSuperinterfaces(baseDtoSuperInterface(definition))
                .addProperties(constructorProperties)
                .addFunction(overrideToEntityMethod(definition))
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
        return superClass
            .filter { parentName.contains(it.name) }
            .flatMap { it.fields }
            .map { it.name }
            .any { it == fieldName }
    }

    private fun baseDtoSuperInterface(definition: ClassDefinition): List<ClassName> {
        return superClass
            .filter { definition.parentNames.contains(it.name) }
            .map { ClassName(it.inputDtoPkg, it.inputDtoClassName) }
            .toList()
    }

    private fun jimmerInputInterface(definition: ClassDefinition): ParameterizedTypeName {
        return JimmerMember.inputInterface.parameterizedBy(ClassName(definition.packageName, definition.name))
    }

    private fun overrideToEntityMethod(definition: ClassDefinition): FunSpec {

        val builder = FunSpec.builder("toEntity")
            .addModifiers(KModifier.OVERRIDE)
            .returns(ClassName(definition.packageName, definition.name))
            .addCode("return ")
            .addStatement("%M(%L::class).by{", JimmerMember.newFun, definition.name)
        definition.fields.forEach {
            if (it.jimmerFieldRestrict.isIdViewListField) {
                return@forEach
            }
            if (it.jimmerFieldRestrict.isRelation) {
                relationFields(builder, it, definition)
                return@forEach
            }
            builder
                .addStatement("this@%L.%L?.let{", definition.inputDtoClassName, it.name)
                .addStatement("%L = it", it.name)
                .addStatement("}")
        }
        return builder.addStatement("}").build()
    }

    private fun relationFields(builder: FunSpec.Builder, field: FieldDefinition, definition: ClassDefinition) {
        if (field.isListField) {
            builder
                .addStatement("this@%L.%L.forEach{", definition.inputDtoClassName, field.name)
                .addStatement("%L().addBy{ it.toEntity() }", field.name)
                .addStatement("}")
        } else {
            builder
                .addStatement("this@%L.%L?.let{", definition.inputDtoClassName, field.name)
                .addStatement("%L = it.toEntity()", field.name)
                .addStatement("}")
        }
    }
}