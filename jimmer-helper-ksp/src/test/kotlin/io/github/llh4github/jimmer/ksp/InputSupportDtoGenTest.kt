package io.github.llh4github.jimmer.ksp

import com.tschuchort.compiletesting.SourceFile
import io.github.llh4github.jimmer.ksp.generator.InputSupportDtoGen
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 *
 *
 * Created At 2023/9/12 14:33
 * @author llh
 */
class InputSupportDtoGenTest : BaseTest() {

    @Test
    fun `gen simple input dto`() {
        val source = SourceFile.kotlin(
            "Test.kt",
            simpleEntityWithBaseClass
        )
        val result = compile(TestProcessorProvider(InputSupportDtoGen), source)
        assertEquals(1, result.size)
        val outcome = """
package io.github.llh4github.app.entity.helper

import java.time.LocalDateTime
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress

/** 由 jimmer-helper-ksp 插件生成。请勿修改。 */
@Suppress("RedundantVisibilityModifier", "Unused")
public data class PersonInput(
    public var name: String? = null,
    public var age: Int? = null,
    public var id: Long? = null,
    public var createdTime: LocalDateTime? = null
)
        """.trimIndent()
        assertEquals(outcome, result[0].trimIndent())
    }

    @Test
    fun `relation model gen test`() {
        val source = SourceFile.kotlin(
            "Test2.kt",
            twoModelRelation
        )
        val result = compile(TestProcessorProvider(InputSupportDtoGen), source)
        assertEquals(2, result.size)
    }
}