package com.github.llh4github.jimmerhelper.core.dto

/**
 *
 *
 * Created At 2023/6/6 17:39
 * @author llh
 */
data class ClassDefinition(
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
){
    /**
     * input-dto辅助类名
     */
    val inputDtoClassName = "${className}Input"
}
