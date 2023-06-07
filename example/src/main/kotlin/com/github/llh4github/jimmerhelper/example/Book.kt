package com.github.llh4github.jimmerhelper.example

import org.babyfish.jimmer.sql.*
import javax.validation.constraints.Size

/**
 *
 * Created At 2023/6/6 17:55
 * @author llh
 */
@Entity
interface Book : BaseModel {

    @get:Size(max = 50)
    val name: String

    val price: Int

    @ManyToMany
    @JoinTable(
        name = "BOOK_AUTHOR_MAPPING",
        joinColumnName = "BOOK_ID",
        inverseJoinColumnName = "AUTHOR_ID"
    )
    val authors: List<Author>

    @ManyToOne
    val store: BookStore?

    @IdView
    val storeId: Int?

    @IdView("authors")
    val authorIds: List<Int>
}