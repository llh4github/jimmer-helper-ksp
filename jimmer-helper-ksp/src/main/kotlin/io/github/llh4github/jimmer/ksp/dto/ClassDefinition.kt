package io.github.llh4github.jimmer.ksp.dto

import io.github.llh4github.jimmer.ksp.common.inputDtoPkgName
import io.github.llh4github.jimmer.ksp.common.inputDtoSuffix

/**
 *
 *
 * Created At 2023/9/11 17:15
 * @author llh
 */
data class ClassDefinition(
    /**
     * 类名
     */
    val name: String,
    /**
     * 包名
     */
    val packageName: String,

    val isJimmerModel: Boolean = false,

    /**
     * 是否被[io.github.llh4github.core.ToJimmerEntity]注解修饰
     */
    val isHelperDtoClass: Boolean = false,

    /**
     * 类注释
     */
    val doc: String? = null,

    /**
     * 字段信息
     */
    val fields: List<FieldDefinition> = emptyList(),

    /**
     * 父类名称全集
     */
    val parentNames: List<String> = emptyList(),

    /**
     * 是否是被[org.babyfish.jimmer.sql.MappedSuperclass]注解修饰的类
     */
    val isMappedSuperclass: Boolean = false,

    val helperAnnoInfo: ToJimmerAnnoInfo? = null,
) {

    /**
     * input-dto辅助类名
     */
    val inputDtoClassName = "${name}$inputDtoSuffix"

    /**
     * Jimmer框架的Draft对象
     */
    val draftClass = "${name}Draft"

    /**
     * input-dto 辅助类的包名
     */
    val inputDtoPkg = "${packageName}.$inputDtoPkgName"

    /**
     * 当前类被标记为忽略的字段名
     */
    val ignoreFields: List<String> by lazy {
        val rs = mutableListOf<String>()
        helperAnnoInfo?.let {
            rs.addAll(it.ignoreFields)
        }
        fields.asSequence()
            .filterNot { it.helperFieldRestrict == null }
            .filter { it.helperFieldRestrict!!.ignore }
            .map { it.name }
            .distinct()
            .toList()
            .takeIf { it.isNotEmpty() }?.let {
                rs.addAll(it)
            }
        rs
    }
}

data class ToJimmerAnnoInfo(
    val targetType: TypeInfo,
    val ignoreFields: List<String>,
)
