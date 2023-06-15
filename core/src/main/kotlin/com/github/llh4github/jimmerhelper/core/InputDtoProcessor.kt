package com.github.llh4github.jimmerhelper.core

import com.facebook.ktfmt.format.Formatter
import com.facebook.ktfmt.format.FormattingOptions
import com.github.llh4github.jimmerhelper.core.common.logger
import com.github.llh4github.jimmerhelper.core.extract.extractClassInfo
import com.github.llh4github.jimmerhelper.core.generator.InputClassGen
import com.github.llh4github.jimmerhelper.core.generator.SuperInterfaceGen
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.pinterest.ktlint.rule.engine.api.Code
import com.pinterest.ktlint.rule.engine.api.EditorConfigDefaults
import com.pinterest.ktlint.rule.engine.api.EditorConfigOverride
import com.pinterest.ktlint.rule.engine.api.KtLintRuleEngine
import com.pinterest.ktlint.rule.engine.core.api.*
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.INDENT_SIZE_PROPERTY
import com.pinterest.ktlint.rule.engine.core.api.editorconfig.INDENT_STYLE_PROPERTY
import com.squareup.kotlinpoet.FileSpec
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicBoolean

/**
 *
 * Created At 2023/6/6 17:06
 * @author llh
 */
class InputDtoProcessor(private val codeGenerator: CodeGenerator) : SymbolProcessor {
    private val processed = AtomicBoolean()
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (!processed.compareAndSet(false, true)) {
            return emptyList()
        }
        logger.info("process start")
        val files = resolver.getAllFiles()

        extractClassInfo(files)
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
//        val options = FormattingOptions(style = FormattingOptions.Style.GOOGLE, maxWidth = 120, blockIndent = 4)

        // Remove tailing commas in parameter lists.
        val code = fileSpec.toString().replace(Regex(""",\s*\)"""), ")")
        return try {
//            Formatter.format(options, code)
//            KtLintRuleEngine().format(Code.fromSnippet(code))
            code
        } catch (e: Exception) {
            logger.exception(e)
            code
        }
    }
}
internal val CUSTOM_RULE_SET_ID = "custom-rule-set-id"

internal val KTLINT_API_CONSUMER_RULE_PROVIDERS =
    setOf(
        // Can provide custom rules
        RuleProvider { NoVarRule() },
        // but also reuse rules from KtLint rulesets
//        RuleProvider { IndentationRule() },
    )
public class NoVarRule : Rule(
    ruleId = RuleId("$CUSTOM_RULE_SET_ID:no-var"),
    about = About(),
) {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if (node.elementType == ElementType.VAR_KEYWORD) {
            emit(node.startOffset, "Unexpected var, use val instead", false)
        }
    }
}