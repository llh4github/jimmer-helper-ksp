package com.github.llh4github.jimmerhelper.ksp.generator

import com.github.llh4github.jimmerhelper.ToJimmerEntity
import com.github.llh4github.jimmerhelper.ToJimmerEntityField
import com.github.llh4github.jimmerhelper.ksp.common.*
import com.github.llh4github.jimmerhelper.ksp.dto.ClassInfoDto
import com.github.llh4github.jimmerhelper.ksp.dto.ConvertExtFunAnnoInfo
import com.github.llh4github.jimmerhelper.ksp.dto.ConvertTargetInfo
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toTypeName
import org.babyfish.jimmer.ksp.annotation
import org.babyfish.jimmer.ksp.annotations
import org.babyfish.jimmer.ksp.name

private val fileBuilderMap: MutableMap<String, FileSpec.Builder> = mutableMapOf()
fun toJimmerEntityExtFunGen(pojoList: List<ClassInfoDto>, jimmerEntities: List<ClassInfoDto>): List<FileSpec> {
    pojoList.forEach {
        toJimmerEntityExtFunGen(it, jimmerEntities)
    }
    return fileBuilderMap.values.map { it.build() }.toList()
}

fun toJimmerEntityExtFunGen(dto: ClassInfoDto, jimmerEntities: List<ClassInfoDto>) {
    val info = parseConvertExtFunAnnoArguments(dto.classDeclaration) ?: return
    parseIgnoreFields(dto.classDeclaration.getAllProperties())
        .takeIf { it.isNotEmpty() }?.let {
            info.addIgnoreField(it)
        }
    parseRenameFields(dto.classDeclaration.getAllProperties())
        .takeIf { it.isNotEmpty() }?.let {
            info.renameFields.addAll(it)
        }
    fillExtFunFile(info, dto, jimmerEntities)
}


/**
 * 按模块填充拓展函数文件内容
 */
private fun fillExtFunFile(
    info: ConvertExtFunAnnoInfo,
    classInfoDto: ClassInfoDto,
    jimmerEntities: List<ClassInfoDto>
) {
    val builder = fileBuilder(info.pkgName)
        .addImport(info.targetPkgName, "by")
    val toEntityFun = "toJimmerEntity"
    val toEntityBuilderFun = "toJimmerEntityBuilder"
    val draftClass = ClassName(info.targetPkgName, info.draftClass, JIMMER_BUILDER_INNER_CLASS)
    val entityBuilderFunBuilder = FunSpec.builder(toEntityBuilderFun)
        .receiver(ClassName(info.pkgName, info.className))
        .addAnnotation(suppressWarns)
        .returns(draftClass)

    val toEntityFunBuilder = FunSpec.builder(toEntityFun)
        .receiver(ClassName(info.pkgName, info.className))
        .addAnnotation(suppressWarns)
    val entityFunReturn = CodeBlock.builder()
        .addStatement("%M(%L::class).by{", JimmerMember.newFun, info.targetClassName)
    val comment = CodeBlock.builder()
        .addStatement("此拓展方法由插件生成。根据提供的字段名转换为Jimmer数据库模型类实例。\n")
        .addStatement(" 已经转换字段的有： \n")
    val entityBuilderFunReturn = CodeBlock.builder()
        .addStatement("val rs = %L()", info.draftBuilderClass)
    classInfoDto.fields
        .filter { !it.isList }
        .filter {
//            logger.info("${it.name}需要忽略？ ${!info.ignoreFields.contains(it.name)}")
            !info.ignoreFields.contains(it.name)
        }
        .forEach {
            val fieldName = info.findFieldName(it.name)
            logger.info("${it.name} -> $fieldName")
            val isIn = isFieldInTargetClass(fieldName, info.targetInfo, jimmerEntities)

            if (isIn) {
                if (it.nullable) {
                    entityFunReturn
                        .addStatement("this@%L.%L?.let{", toEntityFun, it.name)
                        .addStatement("%L = it", fieldName)
                        .addStatement("}")
                    entityBuilderFunReturn
                        .addStatement("this@%L.%L?.let{", toEntityBuilderFun, it.name)
                        .addStatement("rs.%L(it)", fieldName)
                        .addStatement("}")
                } else {
                    entityFunReturn
                        .addStatement("%L = this@%L.%L", fieldName, toEntityFun, it.name)

                    entityBuilderFunReturn
                        .addStatement("rs.%L(this@%L.%L)", fieldName, toEntityBuilderFun,it.name)
                }
                comment.addStatement("- %L -> %L \n", it.name, fieldName)
            }
        }

    entityFunReturn.addStatement("}")
    toEntityFunBuilder
        .addKdoc(comment.build())
        .addStatement("val rs = ")
        .addCode(entityFunReturn.build())
        .addStatement("return rs")
        .returns(ClassName(info.targetPkgName, info.targetClassName))
    entityBuilderFunBuilder
        .addKdoc(comment.build())
        .addCode(entityBuilderFunReturn.build())
        .addStatement("return rs")
    builder
        .addFunction(toEntityFunBuilder.build())
        .addFunction(entityBuilderFunBuilder.build())
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

/**
 * 字段名是否存在于目标类中
 */
private fun isFieldInTargetClass(
    field: String,
    target: ConvertTargetInfo,
    jimmerEntities: List<ClassInfoDto>
): Boolean {
    return jimmerEntities.filter { it.packageName == target.pkgName }
        .filter { it.className == target.name }
        .flatMap { it.fields }
        .any { it.name == field }
}

/**
 * 解析[com.github.llh4github.jimmerhelper.ToJimmerEntity]注解的参数值。
 */
private fun parseConvertExtFunAnnoArguments(
    classDeclaration: KSClassDeclaration
): ConvertExtFunAnnoInfo? {
    val anno = classDeclaration.annotation(ToJimmerEntity::class) ?: return null
    val kclass = anno.arguments.getValue(ToJimmerEntityProperties.jimmerEntity) as KSType
    val targetInfo = ConvertTargetInfo(
        kclass.declaration.simpleName.asString(),
        kclass.declaration.packageName.asString(),
    )

    val b = anno.arguments.getValue(ToJimmerEntityProperties.ignoreFields) as List<*>
    val info = ConvertExtFunAnnoInfo(
        classDeclaration.simpleName.asString(),
        classDeclaration.packageName.asString(),
        targetInfo
    )
    info.addIgnoreField(b)
    return info
}


private fun parseRenameFields(properties: Sequence<KSPropertyDeclaration>): List<Pair<String, String>> {
    return properties.map {
        it.annotations { ele ->
            val annoClassName = ele.annotationType.toTypeName()
            annoClassName == ToJimmerEntityField::class.asClassName()
        }.map { ele ->
            val ignore = ele.arguments.getValue(ToJimmerEntityFieldProperties.ignore) as Boolean
            if (!ignore) {
                val rename = ele.arguments.getValue(ToJimmerEntityFieldProperties.rename) as String
                Pair(it.name, rename)
            } else {
                null
            }
        }.firstOrNull()
    }
        .filter { it != null }
        .map { it!! }
        .toList()
}

private fun parseIgnoreFields(properties: Sequence<KSPropertyDeclaration>): List<String> {
    return properties.filter {
        it.annotations { ele ->
            val annoClassName = ele.annotationType.toTypeName()
            annoClassName == ToJimmerEntityField::class.asClassName()
        }.map { ele ->
//            logger.info("字段 ${it.name} 是否要忽略： ${ele.arguments.getValue(ToJimmerEntityFieldProperties.ignore)} ")
            (ele.arguments.getValue(ToJimmerEntityFieldProperties.ignore) as Boolean)
        }.firstOrNull() ?: false
    }
        .map { it.name }
        .toList()
}

