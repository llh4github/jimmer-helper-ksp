package com.github.llh4github.jimmerhelper.core

import com.github.llh4github.jimmerhelper.core.common.OptionKey
import com.github.llh4github.jimmerhelper.core.common.inputDtoSuffix
import com.github.llh4github.jimmerhelper.core.common.logger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 *
 * Created At 2023/6/6 17:05
 * @author llh
 */
class InputDtoProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        logger = environment.logger
        inputDtoSuffix = environment.options.getOrDefault(OptionKey.inputDtoSuffixKey, "Input")

        environment.options.get(OptionKey.hello)?.let {
            logger.info(" hello $it, This is Jimmer-Helper-KSP.")
        }
        logger.info("生成 inputDto 对象插件运行成功")
        return InputDtoProcessor()
    }
}