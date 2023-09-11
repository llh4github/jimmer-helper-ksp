package io.github.llh4github.jimmer.ksp.common

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.sql.*


/**
 * jimmer 框架中的常用注解
 */
object JimmerAnno {
    val entity = Entity::class
    val transient = Transient::class
    val id = Id::class
    val key = Key::class
    val manyToOne = ManyToOne::class
    val manyToMany = ManyToMany::class
    val oneToOne = OneToOne::class
    val oneTyMany = OneToMany::class
    val superclass = MappedSuperclass::class
    val formula = Formula::class
    val idView = IdView::class
}

/**
 * jimmer框架中类与方法
 */
object JimmerMember {
    val newFun = MemberName("org.babyfish.jimmer.kt", "new")
    val newFetcherFun = MemberName("org.babyfish.jimmer.sql.kt.fetcher", "newFetcher")
    val fetcherClass = ClassName("org.babyfish.jimmer.sql.fetcher", "Fetcher")
    val ktSqlClient = ClassName("org.babyfish.jimmer.sql.kt", "KSqlClient")

    val inputInterface = ClassName("org.babyfish.jimmer", "Input")
}

/**
 * 关系映射注解
 */
val relationAnnoList =
    listOf(JimmerAnno.manyToOne, JimmerAnno.manyToMany, JimmerAnno.oneToOne, JimmerAnno.oneTyMany)

val relationListAnnoList = listOf(JimmerAnno.manyToMany)

/**
 * Jimmer框架中合法的实体注解
 */
val JimmerEntityAnno = listOf(JimmerAnno.entity, JimmerAnno.superclass)

/**
 * 下列注解暂不生成对应字段
 */
val ignoreAnnoList =
    listOf(JimmerAnno.transient, JimmerAnno.formula)


/**
 * [io.github.llh4github.core.ToJimmerEntityField]注解的属性名
 */
object ToJimmerEntityFieldProperties {
    const val ignore = "ignore"
    const val rename = "rename"
}

/**
 * [io.github.llh4github.core.ToJimmerEntity]注解的属性名
 */
object ToJimmerEntityProperties {

    const val jimmerEntity = "jimmerEntity"
    const val ignoreFields = "ignoreFields"
}

/**
 * Jimmer框架Draft对象内构建类名
 */
const val JIMMER_BUILDER_INNER_CLASS = "MapStruct"