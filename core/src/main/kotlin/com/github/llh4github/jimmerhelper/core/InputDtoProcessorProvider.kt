package com.github.llh4github.jimmerhelper.core

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
        environment.logger.info("生成 inputDto 对象插件运行成功")
        TODO("Not yet implemented")
    }
}