package io.github.llh4github.jimmer.ksp.dto

/**
 *
 *
 * Created At 2023/9/11 10:21
 * @author llh
 */
data class FieldDefinition(
    /**
     * 字段名
     */
    val name: String,
    /**
     * 此字段是否为可空类型
     */
    val nullable: Boolean = false,
    /**
     * 字段的注释
     */
    val doc: String? = null,

    /**
     * 字段类型信息
     */
    val typeInfo: TypeInfo,
    /**
     * 字段泛型参数(只支持单个)
     */
    val genericParam: TypeInfo? = null,
    /**
     * Jimmer框架对此字段的约束
     */
    val jimmerFieldRestrict: JimmerFieldRestrict = JimmerFieldRestrict(),
    /**
     * 辅助插件对此字段的约束
     */
    val helperFieldRestrict: HelperFieldRestrict? = null,
) {
    /**
     * 当前字段是否为List类型
     */
    val isListField = genericParam != null
}

data class TypeInfo(

    /**
     * 类型包名
     */
    val typePackage: String,

    /**
     * 字段类型名
     */
    val typeName: String,
)

data class JimmerFieldRestrict(

    /**
     * 此字段是否被[org.babyfish.jimmer.sql.Key]注解修饰
     */
    val isJimmerKey: Boolean = false,

    /**
     * 此字段是否为主键
     */
    val isPrimaryKey: Boolean = false,

    val isRelation: Boolean = false,

    /**
     * 是否为集合类型的IdView字段
     */
    val isIdViewListField: Boolean = false,
)

data class HelperFieldRestrict(
    val rename: String,
    val ignore: Boolean = false,
)
