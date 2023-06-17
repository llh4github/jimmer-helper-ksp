package com.github.llh4github.jimmerhelper.ksp.dto

/**
 * 生成转换拓展方法[com.github.llh4github.jimmerhelper.ToJimmerEntity]注解的参数信息
 *
 * Created At 2023/6/16 16:52
 * @author llh
 */
data class ConvertExtFunAnnoInfo(
    /**
     * 要转换为的Jimmer实体类名
     */
    val className: String,

    val targetInfo: ConvertTargetInfo,
    /**
     * 不需要转换的字段列表
     */
    val ignoreField: MutableList<String> = mutableListOf(),
) {
    fun addIgnoreField(field: String) {
        ignoreField.add(field)
    }

    fun addIgnoreField(fields: List<*>) {
        val list = fields.map { it as String }
            .toList()
        ignoreField.addAll(list)
    }
}

data class ConvertTargetInfo(
    /**
     * 转换目标类的名称
     */
    val name: String,
    /**
     * 转换目标类的包名
     */
    val pkgName: String,

    )