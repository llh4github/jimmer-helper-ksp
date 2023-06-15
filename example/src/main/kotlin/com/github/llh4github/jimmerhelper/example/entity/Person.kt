package com.github.llh4github.jimmerhelper.example.entity

import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Key
import java.time.LocalDateTime

/**
 *
 * Created At 2023/6/15 14:53
 * @author llh
 */
@Entity
interface Person {

    @Id
    val id: Long

    @Key
    val name: String

    val age: Int

    val createdTime: LocalDateTime
}