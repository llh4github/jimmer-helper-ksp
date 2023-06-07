package com.github.llh4github.jimmerhelper.example

import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime

/**
 *
 * Created At 2023/6/6 20:16
 * @author llh
 */
@MappedSuperclass
interface BaseModel {
    @Id
    val id: Int

    val createdTime: LocalDateTime

    val updatedBy: Int?
}