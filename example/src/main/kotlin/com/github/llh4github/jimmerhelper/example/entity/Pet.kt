package com.github.llh4github.jimmerhelper.example.entity

import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Id

/**
 *
 * Created At 2023/6/15 14:55
 * @author llh
 */
@Entity
interface Pet {
    @Id
    val id: Long

    val name: String
}