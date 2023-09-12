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


    //region 输入案例

    val simpleEntityWithBaseClass =
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
                }

    """.trimIndent()

    val twoModelRelation = """
package io.github.llh4github.app.model

import org.babyfish.jimmer.sql.*
import java.time.LocalDateTime

@MappedSuperclass
interface BaseModel {
    @Id
    val id: Int
    val createdTime: LocalDateTime
    val updatedBy: Int?
}
enum class Gender {
    @EnumItem(name = "M")
    MALE,

    @EnumItem(name = "F")
    FEMALE
}
@Entity
interface Author : BaseModel {
    @Key
    val firstName: String
    @Key
    val lastName: String
    val gender: Gender
}
@Entity
interface Book : BaseModel {
    val name: String
    val price: Int
    @ManyToMany
    @JoinTable(
        name = "BOOK_AUTHOR_MAPPING",
        joinColumnName = "BOOK_ID",
        inverseJoinColumnName = "AUTHOR_ID"
    )
    val authors: List<Author>
    @IdView("authors")
    val authorIds: List<Int>
}
    """.trimIndent()
    //endregion 输入案例
}