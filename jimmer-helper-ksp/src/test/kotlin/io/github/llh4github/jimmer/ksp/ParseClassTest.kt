package io.github.llh4github.jimmer.ksp

import com.tschuchort.compiletesting.SourceFile
import org.junit.jupiter.api.Test

/**
 *
 *
 * Created At 2023/9/11 11:54
 * @author llh
 */
class ParseClassTest : BaseTest() {

    @Test
    fun a() {
        val source = SourceFile.kotlin(
            "Test.kt",
            """
                package io.github.llh4github.app.entity

                import org.babyfish.jimmer.sql.Entity
                import org.babyfish.jimmer.sql.Id
                import org.babyfish.jimmer.sql.Key
                import java.math.BigDecimal
                import java.time.LocalDateTime

                @Entity
                interface Person {
                    @Id
                    /** id */
                    val id: Long
                    @Key
                    /** 名称 */
                    val name: String
                    val age: Int?
                    val info:List<String>
                    val other:List<BigDecimal>
                    val createdTime: LocalDateTime
                }
            """.trimIndent()
        )
        val result = compile(source)
        println(result.exitCode)
    }
}