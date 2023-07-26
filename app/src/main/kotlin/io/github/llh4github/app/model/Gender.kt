package io.github.llh4github.app.model

import org.babyfish.jimmer.sql.EnumItem

/**
 *
 *
 * Created At 2023/6/7 20:41
 * @author llh
 */
enum class Gender {
    @EnumItem(name = "M")
    MALE,

    @EnumItem(name = "F")
    FEMALE
}
