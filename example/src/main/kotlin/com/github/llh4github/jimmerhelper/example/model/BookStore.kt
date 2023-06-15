package com.github.llh4github.jimmerhelper.example.model

import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.OneToMany
import org.babyfish.jimmer.sql.Transient
import java.math.BigDecimal

/**
 *
 * Created At 2023/6/7 20:44
 * @author llh
 */
@Entity
interface BookStore : BaseModel, TenantAware {
    @Key
    val name: String

    val website: String?

    @OneToMany(mappedBy = "store")
    val books: List<Book>

    @Transient
    val avgPrice: BigDecimal

    @Transient
    val newestBooks: List<Book>
}