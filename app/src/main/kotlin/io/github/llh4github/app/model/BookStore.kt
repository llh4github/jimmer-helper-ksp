package io.github.llh4github.app.model

import io.github.llh4github.app.model.BaseModel
import io.github.llh4github.app.model.Book
import io.github.llh4github.app.model.TenantAware
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