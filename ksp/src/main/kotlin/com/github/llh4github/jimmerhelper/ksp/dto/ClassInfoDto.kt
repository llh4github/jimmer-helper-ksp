package com.github.llh4github.jimmerhelper.ksp.dto

import com.github.llh4github.jimmerhelper.ksp.common.inputDtoPkgName
import com.github.llh4github.jimmerhelper.ksp.common.inputDtoSuffix
import com.google.devtools.ksp.symbol.KSClassDeclaration

/**
 *
 *
 * Created At 2023/6/6 17:39
 * @author llh
 */
data class ClassInfoDto(
    /**
     * 类名
     */
    val className: String,
    /**
     * 包名
     */
    val packageName: String,
    /**
     * KSP解析出的类定义信息
     */
    val classDeclaration: KSClassDeclaration,
    /**
     * 类注释
     */
    val doc: String? = null,
    /**
     * 是否是被[org.babyfish.jimmer.sql.MappedSuperclass]注解修饰的类
     */
    val isSupperClass: Boolean = false,
    /**
     * 字段信息
     */
    val fields: List<FieldInfoDto> = emptyList(),
    /**
     * 父类名称全集
     */
    val parentNames: List<String> = emptyList(),
) {
    /**
     * input-dto辅助类名
     */
    val inputDtoClassName = "${className}$inputDtoSuffix"

    /**
     * input-dto 辅助类的包名
     */
    val inputDtoPkg = "${packageName}.$inputDtoPkgName"

    /**
     * Jimmer框架的Draft对象
     */
    val draftClass = "${className}Draft"

    val draftBuilderClass = "${draftClass}.MapStruct"

}
