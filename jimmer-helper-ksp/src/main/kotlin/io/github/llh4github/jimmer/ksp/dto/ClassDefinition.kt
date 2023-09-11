package io.github.llh4github.jimmer.ksp.dto

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
    val isHelperDtoClass:Boolean =false,

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
)