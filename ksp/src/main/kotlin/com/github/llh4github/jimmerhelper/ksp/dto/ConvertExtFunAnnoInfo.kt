package com.github.llh4github.jimmerhelper.ksp.dto


/**
 * 生成转换拓展方法[com.github.llh4github.jimmerhelper.ToJimmerEntity]注解的参数信息
 *
 * Created At 2023/6/16 16:52
 * @author llh
 */
data class ConvertExtFunAnnoInfo(
    /**
     * 被标记的类名
     */
    val className: String,
    /**
     * 被标记类的包名
     */
    val pkgName:String,

    val targetInfo: ConvertTargetInfo,
    /**
     * 不需要转换的字段列表
     */
    val ignoreFields: MutableList<String> = mutableListOf(),
    /**
     * 需要重命名的字段列表
     *
     * 原字段名-重命名后的名
     */
    val renameFields: MutableList<Pair<String, String>> = mutableListOf(),
) {
    val targetClassName = targetInfo.name
    val targetPkgName = targetInfo.pkgName
    fun addIgnoreField(field: String) {
        ignoreFields.add(field)
    }

    fun addIgnoreField(fields: List<*>) {
        val list = fields.map { it as String }
            .toList()
        ignoreFields.addAll(list)
    }

    /**
     * 查询字段名。如果被重命名则返回重命名后的，如没有则返回原名称。
     */
    fun findFieldName(name: String): String {
        return renameFields.filter { it.first == name }
            .map { it.second }
            .firstOrNull() ?: name
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