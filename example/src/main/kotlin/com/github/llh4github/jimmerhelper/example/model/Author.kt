package com.github.llh4github.jimmerhelper.example.model

import org.babyfish.jimmer.Formula
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToMany

/**
 *
 * Created At 2023/6/7 20:40
 * @author llh
 */
@Entity
interface Author : BaseModel {
    @Key
    val firstName: String

    @Key
    val lastName: String

    val gender: Gender

    @ManyToMany(mappedBy = "authors")
    val books: List<Book>

    @Formula(dependencies = ["firstName", "lastName"])
    val fullName: String
        get() = "$firstName $lastName"
}