package io.github.llh4github.jimmer.ksp

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.jetbrains.kotlin.config.JvmTarget

/**
 *
 *
 * Created At 2023/9/11 10:45
 * @author llh
 */
abstract class BaseTest {
    protected fun compile(
        provider: SymbolProcessorProvider = JimmerHelperProcessorProvider(),
        vararg sourceFiles: SourceFile
    ): KotlinCompilation.Result {
        return prepareCompilation(provider, *sourceFiles).compile()
    }

    protected fun compile(
        provider: TestProcessorProvider,
        vararg sourceFiles: SourceFile
    ): List<String> {
        prepareCompilation(provider, *sourceFiles).compile()
        return provider.outcome
    }

    private fun prepareCompilation(
        provider: SymbolProcessorProvider,
        vararg sourceFiles: SourceFile
    ): KotlinCompilation {
        return KotlinCompilation().apply {
            sources = sourceFiles.asList()
            symbolProcessorProviders = listOf(provider)
            inheritClassPath = true
            sources = sourceFiles.asList()
            verbose = false
            jvmTarget = JvmTarget.JVM_11.description
        }
    }
}