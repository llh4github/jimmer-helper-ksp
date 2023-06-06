package com.github.llh4github.jimmerhelper.example

import org.babyfish.jimmer.Immutable
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Id
import javax.validation.constraints.Size

/**
 *
 * Created At 2023/6/6 17:55
 * @author llh
 */
@Entity
interface Book {


    @Id
    val id: Int

    @get:Size(max = 50)
    val name: String


    val price: Int

}