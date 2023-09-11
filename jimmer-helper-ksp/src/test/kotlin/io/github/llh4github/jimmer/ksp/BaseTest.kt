package io.github.llh4github.jimmer.ksp

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
    protected fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result {
        return prepareCompilation(*sourceFiles).compile()
    }

    private fun prepareCompilation(vararg sourceFiles: SourceFile): KotlinCompilation {
        return KotlinCompilation().apply {
            sources = sourceFiles.asList()
            symbolProcessorProviders = listOf(JimmerHelperProcessorProvider())
            inheritClassPath = true
            sources = sourceFiles.asList()
            verbose = false
            jvmTarget = JvmTarget.JVM_11.description
        }
    }
}