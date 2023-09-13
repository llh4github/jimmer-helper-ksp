package io.github.llh4github.jimmer.ksp

import com.tschuchort.compiletesting.SourceFile
import io.github.llh4github.jimmer.ksp.generator.SimpleFetcherGen
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 *
 *
 * Created At 2023/9/12 18:53
 * @author llh
 */
class SimpleFetcherTest : BaseTest() {
    @Test
    fun `simple fetcher gen test`() {
        val source = SourceFile.kotlin(
            "Test.kt",
            twoModelRelation
        )
        val result = compile(TestProcessorProvider(SimpleFetcherGen), source)
        assertEquals(1, result.size)
        val outcome = """
            // 由插件生成。请勿修改。
            package io.github.llh4github.app.model.helper

            import io.github.llh4github.app.model.Author
            import io.github.llh4github.app.model.Book
            import io.github.llh4github.app.model.`by`
            import kotlin.Suppress
            import org.babyfish.jimmer.sql.fetcher.Fetcher
            import org.babyfish.jimmer.sql.kt.fetcher.newFetcher

            @Suppress("RedundantVisibilityModifier", "Unused")
            public object AuthorSimpleFetcher {
                public val allScalarFields: Fetcher<Author> = newFetcher(Author::class).by { allScalarFields() }

                public val allTableFields: Fetcher<Author> = newFetcher(Author::class).by { allTableFields() }

                public val allKeyProperties: Fetcher<Author> =
                    newFetcher(Author::class).by {
                        firstName()
                        lastName()
                    }
            }

            @Suppress("RedundantVisibilityModifier", "Unused")
            public object BookSimpleFetcher {
                public val allScalarFields: Fetcher<Book> = newFetcher(Book::class).by { allScalarFields() }

                public val allTableFields: Fetcher<Book> = newFetcher(Book::class).by { allTableFields() }
            }
        """.trimIndent()
        assertEquals(outcome, result[0].trimIndent())
    }
}