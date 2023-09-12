package io.github.llh4github.jimmer.ksp

import com.tschuchort.compiletesting.SourceFile
import io.github.llh4github.jimmer.ksp.generator.BaseClassGen
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 *
 *
 * Created At 2023/9/11 11:54
 * @author llh
 */
class BaseClassGenTest : BaseTest() {

    @Test
    fun `generate MappedSuperclass input helper dto`() {
        val source = SourceFile.kotlin(
            "Test.kt",
            """
                package io.github.llh4github.app.entity

                import org.babyfish.jimmer.sql.Entity
                import org.babyfish.jimmer.sql.Id
                import org.babyfish.jimmer.sql.Key
                import java.math.BigDecimal
                import java.time.LocalDateTime

                import io.github.llh4github.core.ToJimmerEntity
                import io.github.llh4github.core.ToJimmerEntityField
                import org.babyfish.jimmer.sql.MappedSuperclass
                
                @MappedSuperclass
                interface Base{
                    @Id
                    /** id */
                    val id: Long
                    val createdTime: LocalDateTime
                }

                @Entity
                interface Person:Base {
                    @Key
                    /** 名称 */
                    val name: String
                    val age: Int?
                    val info:List<String>
                    val other:List<BigDecimal>
                }
            """.trimIndent()
        )
        val result = compile(TestProcessorProvider(BaseClassGen), source)
        assertEquals(1, result.size)
        val expected = """
            package io.github.llh4github.app.entity.helper

            import java.time.LocalDateTime
            import kotlin.Long
            import kotlin.Suppress

            /** 由 jimmer-helper-ksp 插件生成。请勿修改。 */
            @Suppress("RedundantVisibilityModifier", "Unused")
            public interface BaseInput {
                public var id: Long?

                public var createdTime: LocalDateTime?
            }
        """.trimIndent()
        assertEquals(expected, result[0].trimIndent())
    }
}