package com.github.llh4github.jimmerhelper.core.dto

import com.github.llh4github.jimmerhelper.core.common.inputDtoPkgName
import com.github.llh4github.jimmerhelper.core.common.inputDtoSuffix

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
) {
    /**
     * input-dto辅助类名
     */
    val inputDtoClassName = "${className}$inputDtoSuffix "

    /**
     * input-dto 辅助类的包名
     */
    val inputDtoPkg = "${packageName}.$inputDtoPkgName"

}
