package com.github.llh4github.jimmerhelper.example.model

import org.babyfish.jimmer.sql.MappedSuperclass

/**
 *
 * Created At 2023/6/7 20:39
 * @author llh
 */
@MappedSuperclass
interface TenantAware {
    val tenant: String
}