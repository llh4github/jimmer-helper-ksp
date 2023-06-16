package com.github.llh4github.jimmerhelper.ksp.generator

import com.github.llh4github.jimmerhelper.ToJimmerEntity
import com.github.llh4github.jimmerhelper.ToJimmerEntityIgnore
import com.github.llh4github.jimmerhelper.ksp.common.hasAnno
import com.github.llh4github.jimmerhelper.ksp.common.logger
import com.github.llh4github.jimmerhelper.ksp.dto.ClassInfoDto
import com.github.llh4github.jimmerhelper.ksp.dto.ConvertExtFunAnnoInfo
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import org.babyfish.jimmer.ksp.annotation
import org.babyfish.jimmer.ksp.name
import kotlin.reflect.KClass


fun toJimmerEntityExtFunGen(dto: ClassInfoDto) {
    val info = parseConvertExtFunAnnoArguments(dto.classDeclaration) ?: return
    val ignore = parseIgnoreFields(dto.classDeclaration.getAllProperties())
    info.addIgnoreField(ignore)
}

/**
 * 解析[com.github.llh4github.jimmerhelper.ToJimmerEntity]注解的参数值。
 */
private fun parseConvertExtFunAnnoArguments(
    classDeclaration: KSClassDeclaration
): ConvertExtFunAnnoInfo? {
    val anno = classDeclaration.annotation(ToJimmerEntity::class) ?: return null
    val kclass = anno.arguments[0].value as KClass<*>
    logger.info("$kclass")
    val a = anno.arguments[0].value.toString()
    val b = anno.arguments[1].value as List<*>
    val info = ConvertExtFunAnnoInfo(a)
    info.addIgnoreField(b)
    return info
}

private fun parseIgnoreFields(properties: Sequence<KSPropertyDeclaration>): List<String> {
    return properties.filter { it.annotations.hasAnno(ToJimmerEntityIgnore::class) }
        .map { it.name }
        .toList()
}

