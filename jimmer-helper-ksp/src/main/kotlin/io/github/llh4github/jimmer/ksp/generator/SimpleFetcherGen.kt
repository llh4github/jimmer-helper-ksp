package io.github.llh4github.jimmer.ksp.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.github.llh4github.jimmer.ksp.common.JimmerMember
import io.github.llh4github.jimmer.ksp.dto.ClassDefinition

/**
 *
 *
 * Created At 2023/9/12 22:01
 * @author llh
 */
object SimpleFetcherGen : AbstractGenerator() {

    private val fileBuilderMap: MutableMap<String, FileSpec.Builder> = mutableMapOf()
    override fun generate(list: List<ClassDefinition>): List<FileSpec> {
        list.filter { it.isJimmerModel }
            .forEach { fetcherCode(it) }
        return fileBuilderMap.values.map { it.addFileComment("由插件生成。请勿修改。").build() }.toList()
    }

    private fun fetcherCode(definition: ClassDefinition) {
        val fileBuilder = fileBuilder(definition.packageName)
        val builder = TypeSpec.objectBuilder("${definition.name}SimpleFetcher")
            .addAnnotation(suppressWarns)
        builder.addProperty(allScalarFieldsProperty(definition))
        builder.addProperty(allTableFieldsProperty(definition))
        allKeyPropertiesFetcher(definition)?.let {
            builder.addProperty(it)
        }
        fileBuilder.addType(builder.build())
    }

    private fun fetcherReturnType(definition: ClassDefinition): ParameterizedTypeName =
        JimmerMember.fetcherClass.parameterizedBy(ClassName(definition.packageName, definition.name))

    private fun allScalarFieldsProperty(definition: ClassDefinition) =
        PropertySpec.builder("allScalarFields", fetcherReturnType(definition))
            .initializer("%M(%L::class).by{%L}", JimmerMember.newFetcherFun, definition.name, "allScalarFields()")
            .build()

    private fun allTableFieldsProperty(definition: ClassDefinition) =
        PropertySpec.builder("allTableFields", fetcherReturnType(definition))
            .initializer("%M(%L::class).by{%L}", JimmerMember.newFetcherFun, definition.name, "allTableFields()")
            .build()

    private fun allKeyPropertiesFetcher(definition: ClassDefinition): PropertySpec? {
        val list = definition.fields.filter { it.jimmerFieldRestrict.isJimmerKey }.toList()
        if (list.isEmpty()) {
            return null
        }
        val builder = PropertySpec.builder("allKeyProperties", fetcherReturnType(definition))
        val cb = CodeBlock.builder()
            .addStatement("%M(%L::class).by{", JimmerMember.newFetcherFun, definition.name)
        list.forEach {
            cb.addStatement("%L()", it.name)
        }
        cb.addStatement("}")
        builder.initializer(cb.build())
        return builder.build()
    }

    private fun fileBuilder(pkgName: String): FileSpec.Builder {
        if (fileBuilderMap.containsKey(pkgName)) {
            return fileBuilderMap[pkgName]!!
        }
        val builder = FileSpec.builder(
            pkgName, "simple_fetcher"
        )
        fileBuilderMap[pkgName] = builder
        return builder

    }
}