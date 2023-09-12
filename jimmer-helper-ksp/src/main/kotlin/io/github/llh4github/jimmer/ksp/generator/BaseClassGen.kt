package io.github.llh4github.jimmer.ksp.generator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.llh4github.jimmer.ksp.dto.ClassDefinition

/**
 * Jimmer辅助基类生成逻辑
 *
 * 被[org.babyfish.jimmer.sql.MappedSuperclass]注解修饰的类才会生成代码
 *
 * Created At 2023/9/11 21:11
 * @author llh
 */
object BaseClassGen : AbstractGenerator() {

    override fun generate(list: List<ClassDefinition>): List<FileSpec> {
        return list
            .filter { it.isMappedSuperclass }
            .map { generateClass(it) }
            .toList()
    }

    private fun generateClass(definition: ClassDefinition): FileSpec {
        return FileSpec.builder(definition.inputDtoPkg, definition.inputDtoClassName)
            .addType(
                TypeSpec.interfaceBuilder(definition.inputDtoClassName)
                    .addModifiers(KModifier.PUBLIC)
                    .addKdoc(comment)
                    .addAnnotation(suppressWarns)
                    .addProperties(buildProperties(definition))
                    .build()
            ).build()
    }

    private fun buildProperties(definition: ClassDefinition): List<PropertySpec> {
        return definition.fields
            .filter { !it.jimmerFieldRestrict.isIdViewListField } // 暂不处理此类型字段
            .map {
                val type = it.propertyType()
                PropertySpec.builder(it.name, type)
                    .mutable(true)
                    .build()
            }.toList()

    }


}