package com.github.llh4github.jimmerhelper

/**
 * 此字段不参与转换
 *
 * Created At 2023/6/16 17:11
 * @author llh
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class ToJimmerEntityIgnore()
