package io.github.llh4github.jimmer.ksp

import com.tschuchort.compiletesting.SourceFile
import io.github.llh4github.jimmer.ksp.generator.BaseClassGen
import io.github.llh4github.jimmer.ksp.generator.ToJimmerEntityFunGen
import org.junit.jupiter.api.Test

/**
 *
 *
 * Created At 2023/9/12 18:53
 * @author llh
 */
class ToJimmerEntityFunGenTest : BaseTest() {
    @Test
    fun a(){
        val source = SourceFile.kotlin(
            "Test.kt",
            twoModelRelation
        )
        val result = compile(TestProcessorProvider(ToJimmerEntityFunGen), source)
        println(result)
    }
}