package io.github.llh4github.jimmer.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 *
 * Created At 2023/7/26 11:22
 * @author llh
 */
class JimmerHelperProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return JimmerHelperProcessor()
    }
}