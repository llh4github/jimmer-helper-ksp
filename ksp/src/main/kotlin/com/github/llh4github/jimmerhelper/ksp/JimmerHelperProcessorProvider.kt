package com.github.llh4github.jimmerhelper.ksp

import com.github.llh4github.jimmerhelper.ksp.common.OptionKey
import com.github.llh4github.jimmerhelper.ksp.common.inputDtoPkgName
import com.github.llh4github.jimmerhelper.ksp.common.inputDtoSuffix
import com.github.llh4github.jimmerhelper.ksp.common.logger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 *
 * Created At 2023/6/6 17:05
 * @author llh
 */
class JimmerHelperProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        logger = environment.logger
        inputDtoSuffix = environment.options.getOrDefault(OptionKey.inputDtoSuffixKey, "Input")
        inputDtoPkgName = environment.options.getOrDefault(OptionKey.inputDtoPkgNameKey, "helper")

        environment.options.get(OptionKey.hello)?.let {
            logger.info("Hello $it, This is Jimmer-Helper-KSP.")
        }
        logger.info("生成 inputDto 对象插件运行成功")
        return JimmerHelperProcessor(environment.codeGenerator)
    }
}