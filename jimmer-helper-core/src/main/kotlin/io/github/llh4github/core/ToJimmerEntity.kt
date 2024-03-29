package io.github.llh4github.core


import kotlin.reflect.KClass

/**
 * 添加转换成Jimmer对象拓展函数的注解
 *
 * Created At 2023/6/15 16:47
 * @author llh
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ToJimmerEntity(

    /**
     * Jimmer实体接口的引用
     */
    val jimmerEntity: KClass<*>,

    /**
     * 忽略本注解修饰类的某些属性
     */
    val ignoreFields: Array<String> = [],
)
