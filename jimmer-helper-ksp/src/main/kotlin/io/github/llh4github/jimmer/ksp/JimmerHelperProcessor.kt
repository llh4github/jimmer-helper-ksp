package io.github.llh4github.jimmer.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 *
 * Created At 2023/7/26 11:14
 * @author llh
 */
class JimmerHelperProcessor : SymbolProcessor {

    private val processed = AtomicBoolean()
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (!processed.compareAndSet(false, true)) {
            return emptyList()
        }
        // todo 搞完了项目结构再来实现
        return emptyList()
    }
}