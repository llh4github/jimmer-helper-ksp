package io.github.llh4github.jimmer.ksp.common
import com.google.devtools.ksp.processing.KSPLogger
import io.github.llh4github.jimmer.ksp.dto.ClassInfoDto

/**
 * KSP日志对象
 */
lateinit var logger: KSPLogger

/**
 * InputDto类后缀，默认为`XxxInput`
 */
lateinit var inputDtoSuffix: String

/**
 * InputDto类所属包名，默认为`helper`
 */
lateinit var inputDtoPkgName: String

object OptionKey {
    const val inputDtoSuffixKey = "inputDtoSuffix"
    const val inputDtoPkgNameKey = "inputDtoPkgName"
    const val hello = "helloJimmerHelper"
}

/**
 * 父类名称-类信息map
 */
val SUPER_CLASS_MAP = mutableMapOf<String, ClassInfoDto>()
