package com.github.llh4github.jimmerhelper.ksp.generator

import com.github.llh4github.jimmerhelper.ToJimmerEntity
import com.github.llh4github.jimmerhelper.ToJimmerEntityField
import com.github.llh4github.jimmerhelper.ksp.common.logger
import com.github.llh4github.jimmerhelper.ksp.dto.ClassInfoDto
import com.github.llh4github.jimmerhelper.ksp.dto.ConvertExtFunAnnoInfo
import com.github.llh4github.jimmerhelper.ksp.dto.ConvertTargetInfo
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import javaslang.Tuple2
import org.babyfish.jimmer.ksp.annotation
import org.babyfish.jimmer.ksp.annotations
import org.babyfish.jimmer.ksp.name


fun toJimmerEntityExtFunGen(dto: ClassInfoDto) {
    val info = parseConvertExtFunAnnoArguments(dto.classDeclaration) ?: return
    parseIgnoreFields(dto.classDeclaration.getAllProperties())
        .takeIf { it.isNotEmpty() }?.let {
            info.addIgnoreField(it)
        }
    val renameList = parseRenameFields(dto.classDeclaration.getAllProperties())
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

    val a = anno.arguments[0].value.toString()
    val b = anno.arguments[1].value as List<*>
    val info = ConvertExtFunAnnoInfo(a, targetInfo)
    info.addIgnoreField(b)
    return info
}

/**
 * 查询字段名。如果被重命名则返回重命名后的，如没有则返回原名称。
 */
private fun findFieldName(name: String, rename: List<Tuple2<String, String>>): String {
    return rename.filter { it._1 == name }
        .map { it._2 }
        .firstOrNull() ?: name
}

private fun parseRenameFields(properties: Sequence<KSPropertyDeclaration>): List<Tuple2<String, String>> {
    return properties.map {
        it.annotations { ele ->
            val annoClassName = ele.annotationType.toTypeName()
            annoClassName == ToJimmerEntityField::class.asClassName()
        }.map { ele ->
            val ignore = ele.arguments[0].value as Boolean
            val rename = ele.arguments[1].value as String
            logger.info("ccc $rename")
            Tuple2(it.name, rename)
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
        }.firstOrNull() ?: false
    }
        .map { it.name }
        .toList()
}

