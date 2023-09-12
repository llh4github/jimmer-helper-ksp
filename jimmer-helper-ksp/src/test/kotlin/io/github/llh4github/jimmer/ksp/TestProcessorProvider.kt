package io.github.llh4github.jimmer.ksp

import com.facebook.ktfmt.format.Formatter
import com.facebook.ktfmt.format.FormattingOptions
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.FileSpec
import io.github.llh4github.jimmer.ksp.common.OptionKey
import io.github.llh4github.jimmer.ksp.common.inputDtoPkgName
import io.github.llh4github.jimmer.ksp.common.inputDtoSuffix
import io.github.llh4github.jimmer.ksp.common.logger
import io.github.llh4github.jimmer.ksp.generator.AbstractGenerator
import io.github.llh4github.jimmer.ksp.parser.ClassDefinitionParser

/**
 *
 *
 * Created At 2023/9/12 10:08
 * @author llh
 */
class TestProcessorProvider(private val generator: AbstractGenerator) : SymbolProcessorProvider {

    val outcome = mutableListOf<String>()

    inner class Processor : SymbolProcessor {
        override fun process(resolver: Resolver): List<KSAnnotated> {
            val files = resolver.getAllFiles()
            val sequence = convertKsClassSequence(files)
            val classDefinition = ClassDefinitionParser(sequence).parse()
            val list = generator.generate(classDefinition)
                .map { formatCode(it) }
                .toList()
            outcome.addAll(list)
            return emptyList()
        }

        private fun convertKsClassSequence(files: Sequence<KSFile>): Sequence<KSClassDeclaration> {
            return files
                .flatMap { it.declarations.filterIsInstance<KSClassDeclaration>() }
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

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        logger = environment.logger
        inputDtoSuffix = environment.options.getOrDefault(OptionKey.inputDtoSuffixKey, "Input")
        inputDtoPkgName = environment.options.getOrDefault(OptionKey.inputDtoPkgNameKey, "helper")
        return Processor()
    }
}