package com.github.llh4github.jimmerhelper.core.dto

import com.github.llh4github.jimmerhelper.core.common.inputDtoSuffix
import com.squareup.kotlinpoet.ClassName

/**
 *
 *
 * Created At 2023/6/7 16:31
 * @author llh
 */
data class FieldInfoDto(
    /**
     * 字段名
     */
    val name: String,

    /**
     * 类型包名
     */
    val typePackage: String,

    /**
     * 字段类型名
     */
    val typeName: String,


    val complexTypeStr: String? = null,

    /**
     * 此字段是否为主键
     */
    val isPrimaryKey: Boolean = false,
    /**
     * 字段的注释
     */
    val doc: String? = null,

    val isRelationField: Boolean = false,
    /**
     * 此字段是否为List容器
     */
    val isList: Boolean = false,
    /**
     * List的参数类型包名
     */
    val typeParamPkgStr: String? = null,
    /**
     * List的参数类型名
     */
    val typeParamTypeName: String? = null,
){

    val typeParamQualifier: String = "$typeParamTypeName.$typeParamTypeName"
    val isComplexType = complexTypeStr != null

    val typeParamTypeSupportName: String? = if (null != typeParamTypeName) "${typeParamTypeName}$inputDtoSuffix" else null
    fun toClassName(): ClassName {
        return ClassName(typePackage, typeName)
    }
}
