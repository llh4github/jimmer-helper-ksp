package io.github.llh4github.jimmer.ksp.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import io.github.llh4github.jimmer.ksp.common.JIMMER_BUILDER_INNER_CLASS
import io.github.llh4github.jimmer.ksp.dto.ClassDefinition
import io.github.llh4github.jimmer.ksp.dto.TypeInfo

/**
 *
 *
 * Created At 2023/9/12 18:13
 * @author llh
 */
object ToJimmerEntityFunGen : AbstractGenerator() {

    private const val TO_ENTITY_BUILDER_FUN_NAME = "toJimmerEntityBuilder"
    private const val TO_ENTITY_FUN_NAME = "toJimmerEntity"
    private val fileBuilderMap: MutableMap<String, FileSpec.Builder> = mutableMapOf()
    private lateinit var allClassList: List<ClassDefinition>
    override fun generate(list: List<ClassDefinition>): List<FileSpec> {
        allClassList = list
        list
            .filter { it.isHelperDtoClass }
            .forEach { generateExtFun(it) }
        return fileBuilderMap.values.map { it.build() }.toList()
    }


    private fun generateExtFun(definition: ClassDefinition) {
        val targetType = definition.helperAnnoInfo!!.targetType
        val builder = fileBuilder(definition.packageName)
            .addImport(definition.packageName, "by")
        val draftClass = ClassName(targetType.typePackage, targetType.typeName + "Draft", JIMMER_BUILDER_INNER_CLASS)
        val entityBuilderFunBuilder = FunSpec.builder(TO_ENTITY_BUILDER_FUN_NAME)
            .receiver(ClassName(definition.packageName, definition.name))
            .addKdoc(extFunComment(definition))
            .addAnnotation(suppressWarns)
            .addCode(toEntityBuilderFunBody(definition))
            .returns(draftClass)

        builder.addFunction(entityBuilderFunBuilder.build())
            .addFunction(toJimmerEntityFun(definition))
            .addImport(targetType.typePackage, targetType.typeName + "Draft")
    }

    private fun toJimmerEntityFun(definition: ClassDefinition): FunSpec {
        val targetType = definition.helperAnnoInfo!!.targetType
        return FunSpec.builder(TO_ENTITY_FUN_NAME)
            .receiver(ClassName(definition.packageName, definition.name))
            .addKdoc(extFunComment(definition))
            .addAnnotation(suppressWarns)
            .addStatement("return %L().build()", TO_ENTITY_BUILDER_FUN_NAME)
            .returns(ClassName(targetType.typePackage, targetType.typeName))
            .build()
    }

    private fun toEntityBuilderFunBody(definition: ClassDefinition): CodeBlock {
        val targetType = definition.helperAnnoInfo!!.targetType
        val funBody = CodeBlock.builder()
            .addStatement("val rs = %L.MapStruct()", targetType.typeName + "Draft")
        definition.fields
            .filterNot { definition.ignoreFields.contains(it.name) }
            .filter { fieldIsInTargetClass(definition.helperAnnoInfo.targetType, it.name) }
            .forEach {
                if (it.nullable) {
                    funBody
                        .addStatement("this.%L?.let{", it.name)
                        .addStatement("rs.%L(it)", it.actName)
                        .addStatement("}")
                } else {
                    funBody
                        .addStatement("rs.%L(this.%L)", it.actName, it.name)
                }
            }
        return funBody.addStatement("return rs").build()
    }

    private fun fieldIsInTargetClass(target: TypeInfo, fieldName: String): Boolean {
        return allClassList
            .filter { it.name == target.typeName }
            .flatMap { it.fields }
            .any { it.name == fieldName }
    }

    private fun extFunComment(definition: ClassDefinition): CodeBlock {
        val comment = CodeBlock.builder()
            .addStatement("此拓展方法由插件生成。根据提供的字段名转换为Jimmer数据库模型类实例。\n")
            .addStatement(" 已经转换字段的有： \n")
        definition.fields.filter { !it.isListField }
            .filter { !definition.ignoreFields.contains(it.actName) }
            .forEach { comment.addStatement(" - %L -> %L \n", it.name, it.actName) }
        return comment.build()

    }

    private fun fileBuilder(pkgName: String): FileSpec.Builder {
        if (fileBuilderMap.containsKey(pkgName)) {
            return fileBuilderMap[pkgName]!!
        }
        val builder = FileSpec.builder(
            pkgName, "to_jimmer_ext_fun"
        )
        fileBuilderMap[pkgName] = builder
        return builder

    }
}