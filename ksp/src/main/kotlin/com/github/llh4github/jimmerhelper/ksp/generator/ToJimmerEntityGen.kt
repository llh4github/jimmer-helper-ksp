package com.github.llh4github.jimmerhelper.ksp.generator

import com.github.llh4github.jimmerhelper.ToJimmerEntity
import com.github.llh4github.jimmerhelper.ToJimmerEntityField
import com.github.llh4github.jimmerhelper.ksp.common.JimmerMember
import com.github.llh4github.jimmerhelper.ksp.common.logger
import com.github.llh4github.jimmerhelper.ksp.dto.ClassInfoDto
import com.github.llh4github.jimmerhelper.ksp.dto.ConvertExtFunAnnoInfo
import com.github.llh4github.jimmerhelper.ksp.dto.ConvertTargetInfo
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import org.babyfish.jimmer.ksp.annotation
import org.babyfish.jimmer.ksp.annotations
import org.babyfish.jimmer.ksp.name

private val fileBuilderMap: MutableMap<String, FileSpec.Builder> = mutableMapOf()
fun toJimmerEntityExtFunGen(pojoList: List<ClassInfoDto>, jimmerEntities: List<ClassInfoDto>): List<FileSpec> {
    pojoList.forEach {
        logger.info("ccc ${it.className}")
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
    val funName = "toJimmerEntity"
    val funBuilder = FunSpec.builder(funName)
        .receiver(ClassName(info.pkgName, info.className))
        .addAnnotation(suppressWarns)
        .returns(ClassName(info.targetPkgName, info.targetClassName))
        .addCode("return ")
        .addStatement("%M(%L::class).by{", JimmerMember.newFun, info.targetClassName)
    classInfoDto.fields.forEach {
        // 不处理处理集合类
        if (it.isList) {
            return
        }

        val fieldName = info.findFieldName(it.name)
        val isIn = isFieldInTargetClass(fieldName, info.targetInfo, jimmerEntities)
        if (!isIn) {
            return
        }
        if (it.nullable) {
            funBuilder
                .addStatement("this@%L.%L?.let{", funName, it.name)
                .addStatement("%L = it", fieldName)
                .addStatement("}")
        } else {
            funBuilder
                .addStatement("%L = this@%L.%L", fieldName, funName, it.name)
        }
    }

    funBuilder.addStatement("}")
    builder.addFunction(funBuilder.build())
}

private fun fileBuilder(pkgName: String): FileSpec.Builder {
//    if (fileBuilderMap.containsKey(pkgName)) {
//        return fileBuilderMap[pkgName]!!
//    }
    val toInt = Math.random().times(100).toInt()
    val builder = FileSpec.builder(
        pkgName,
        "to_jimmer_ext_fun" + toInt
    )
    fileBuilderMap[pkgName + toInt] = builder
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
    val kclass = anno.arguments[0].value as KSType
    val targetInfo = ConvertTargetInfo(
        kclass.declaration.simpleName.asString(),
        kclass.declaration.packageName.asString(),
    )

    val b = anno.arguments[1].value as List<*>
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
            val ignore = ele.arguments[0].value as Boolean
            val rename = ele.arguments[1].value as String
            logger.info("ccc $rename")
            Pair(it.name, rename)
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
            ele.arguments[0].value as Boolean
        }.any()
    }
        .map { it.name }
        .toList()
}

