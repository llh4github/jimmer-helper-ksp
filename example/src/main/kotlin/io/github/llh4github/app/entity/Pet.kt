package io.github.llh4github.app.entity

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