package com.github.llh4github.jimmerhelper.core

import com.github.llh4github.jimmerhelper.core.common.logger
import com.github.llh4github.jimmerhelper.core.extract.extractClassInfo
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 *
 * Created At 2023/6/6 17:06
 * @author llh
 */
class InputDtoProcessor : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("process start")
        val files = resolver.getAllFiles()
        val classList = extractClassInfo(files)
        logger.info("process end")
        return emptyList()
    }
}