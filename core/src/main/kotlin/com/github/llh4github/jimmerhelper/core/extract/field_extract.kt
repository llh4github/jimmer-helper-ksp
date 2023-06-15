package com.github.llh4github.jimmerhelper.core.extract

import com.github.llh4github.jimmerhelper.core.common.*
import com.github.llh4github.jimmerhelper.core.dto.FieldInfoDto
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeArgument

/**
 * 提取字段信息
 */
internal fun extractFieldInfo(
    properties: Sequence<KSPropertyDeclaration>
): List<FieldInfoDto> {
    return properties
        .filter { !it.annotations.hasAnyAnno(ignoreAnnoList) }
        .map { extractFieldInfo(it) }
        .toList()
}

private fun extractFieldInfo(property: KSPropertyDeclaration): FieldInfoDto {
    val fieldName = property.simpleName.asString()
    val isPrimaryKey = property.annotations.hasAnno(JimmerAnno.id)
    val doc = property.docString
    val tyName = property.type.resolve().declaration.simpleName.asString()
    val typePackage = property.type.resolve().declaration.packageName.asString()
    val isRelation = property.annotations.hasAnyAnno(relationAnnoList)
    var isList = false
    var arg: KSTypeArgument? = null
    var isIdViewListFlag = false
    if ("$typePackage.$tyName" == "kotlin.collections.List") {
        isList = true
        arg = property.type.resolve().arguments[0]
        isIdViewListFlag = property.annotations.hasAnno(JimmerAnno.idView)
    }
    return FieldInfoDto(
        name = fieldName,
        typeName = tyName,
        typePackage = typePackage,
//        complexTypeStr = complexTypeStr,
        isJimmerKey = property.annotations.hasAnno(JimmerAnno.key),
        doc = doc,
        isRelationField = isRelation,
        isList = isList,
        typeParamPkgStr = arg?.typePkg(),
        typeParamTypeName = arg?.typeName(),
        isPrimaryKey = isPrimaryKey,
        isIdViewListField = isIdViewListFlag
    )
}
