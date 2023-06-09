package com.github.llh4github.jimmerhelper.ksp

import com.facebook.ktfmt.format.Formatter
import com.facebook.ktfmt.format.FormattingOptions
import com.github.llh4github.jimmerhelper.ksp.common.logger
import com.github.llh4github.jimmerhelper.ksp.extract.extractJimmerEntityInfo
import com.github.llh4github.jimmerhelper.ksp.extract.extractMyAnnoClassInfo
import com.github.llh4github.jimmerhelper.ksp.generator.InputClassGen
import com.github.llh4github.jimmerhelper.ksp.generator.SuperInterfaceGen
import com.github.llh4github.jimmerhelper.ksp.generator.toJimmerEntityExtFunGen
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.FileSpec
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 * Created At 2023/6/6 17:06
 * @author llh
 */
class JimmerHelperProcessor(private val codeGenerator: CodeGenerator) : SymbolProcessor {
    private val processed = AtomicBoolean()
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (!processed.compareAndSet(false, true)) {
            return emptyList()
        }
        logger.info("process start")
        val files = resolver.getAllFiles()

        val jimmerEntities = extractJimmerEntityInfo(files)
        toJimmerEntityExtFunGen(extractMyAnnoClassInfo(files), jimmerEntities)
            .forEach {
                val file = codeGenerator.createNewFile(
                    Dependencies(aggregating = false),
                    it.packageName, it.name
                )
                file.writer(Charsets.UTF_8).use { out ->
                    out.write(formatCode(it))
                }
            }
        jimmerEntities
            .map {
                if (it.isSupperClass) SuperInterfaceGen(it).build()
                else InputClassGen(it).build()
            }.forEach {
                val file = codeGenerator.createNewFile(
                    Dependencies(aggregating = false),
                    it.packageName, it.name
                )

                file.writer(Charsets.UTF_8).use { out ->
                    out.write(formatCode(it))
                }
            }
        logger.info("process end")
        return emptyList()
    }

    private fun formatCode(fileSpec: FileSpec): String {
        // Use the Kotlin official code style.
        val options = FormattingOptions(
            style = FormattingOptions.Style.FACEBOOK,
            maxWidth = 100,
            blockIndent = 4
        )

        // Remove tailing commas in parameter lists.
        val code = fileSpec.toString().replace(Regex(""",\s*\)"""), ")")
        return try {
            Formatter.format(options, code)
//            code
        } catch (e: Exception) {
            logger.exception(e)
            code
        }
    }
}
