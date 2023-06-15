package com.github.llh4github.jimmerhelper.ksp.common

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.sql.*
import kotlin.reflect.KClass


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
    val eqFun = MemberName("org.babyfish.jimmer.sql.kt.ast.expression", "eq")
    val newFetcherFun = MemberName("org.babyfish.jimmer.sql.kt.fetcher", "newFetcher")
    val fetcherClass = ClassName("org.babyfish.jimmer.sql.fetcher", "Fetcher")
    val ktSqlClient = ClassName("org.babyfish.jimmer.sql.kt", "KSqlClient")
    val saveMode = ClassName("org.babyfish.jimmer.sql.ast.mutation", "SaveMode")

    val valueInFun = MemberName("org.babyfish.jimmer.sql.kt.ast.expression", "valueIn")

    //    fun byFunc(draftClass: String): ClassName {
//        return ClassName(draftClass, "by")
//    }
//
//    fun addByFun(draftClass: String): ClassName {
//        return ClassName(draftClass, "addBy")
//    }
    val inputInterface = ClassName("org.babyfish.jimmer", "Input")

    val kExample = ClassName("org.babyfish.jimmer.sql.kt.ast.query", "KExample")
    val exampleFun = MemberName("org.babyfish.jimmer.sql.kt.ast.query", "example")
}

/**
 * 关系映射注解
 */
val relationAnnoList =
    listOf(JimmerAnno.manyToOne, JimmerAnno.manyToMany, JimmerAnno.oneToOne, JimmerAnno.oneTyMany)
val relationListAnnoList =
    listOf(JimmerAnno.manyToMany)

/**
 * Jimmer框架中合法的实体注解
 */
val JimmerEntityAnno = listOf(JimmerAnno.entity, JimmerAnno.superclass)

/**
 * 下列注解暂不生成对应字段
 */
val ignoreAnnoList =
    listOf(JimmerAnno.transient, JimmerAnno.formula)

object IdType {
    val intKey = Int::class
    val longKey = Long::class
    val stringKey = String::class

    fun idType(typeName: String): KClass<out Any> {
        return when (typeName) {
            "Int" -> intKey
            "Long" -> longKey
            "String" -> stringKey
            else -> Any::class
        }
    }
}