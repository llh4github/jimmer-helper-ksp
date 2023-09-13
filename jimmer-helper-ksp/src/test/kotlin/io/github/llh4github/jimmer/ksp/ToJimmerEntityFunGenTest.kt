package io.github.llh4github.jimmer.ksp

import com.tschuchort.compiletesting.SourceFile
import io.github.llh4github.jimmer.ksp.generator.ToJimmerEntityFunGen
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 *
 *
 * Created At 2023/9/12 18:53
 * @author llh
 */
class ToJimmerEntityFunGenTest : BaseTest() {
    @Test
    fun `ext fun gen test`() {
        val source = SourceFile.kotlin(
            "Test.kt",
            twoModelRelation
        )
        val result = compile(TestProcessorProvider(ToJimmerEntityFunGen), source)
        assertEquals(1, result.size)
    }
}