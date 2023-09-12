package io.github.llh4github.jimmer.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import io.github.llh4github.jimmer.ksp.common.OptionKey
import io.github.llh4github.jimmer.ksp.common.inputDtoPkgName
import io.github.llh4github.jimmer.ksp.common.inputDtoSuffix
import io.github.llh4github.jimmer.ksp.common.logger
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 *
 * Created At 2023/7/26 11:22
 * @author llh
 */
class JimmerHelperProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        logger = environment.logger
        inputDtoSuffix = environment.options.getOrDefault(OptionKey.inputDtoSuffixKey, "Input")
        inputDtoPkgName = environment.options.getOrDefault(OptionKey.inputDtoPkgNameKey, "helper")

        environment.options[OptionKey.hello]?.let {
            logger.info("Hello $it, This is Jimmer-Helper-KSP.")
        }
        logger.info("jimmer-helper插件运行成功")
        return JimmerHelperProcessor(environment.codeGenerator)

    }
}