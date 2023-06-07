package com.github.llh4github.jimmerhelper.core.common

import com.google.devtools.ksp.processing.KSPLogger

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