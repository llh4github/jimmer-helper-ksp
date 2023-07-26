package io.github.llh4github.list

/**
 * 此字段不参与转换
 *
 * Created At 2023/6/16 17:11
 * @author llh
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class ToJimmerEntityField(
    val ignore: Boolean = false,
    val rename: String = "",
)
