package com.github.llh4github.jimmerhelper.example.vo

import com.github.llh4github.jimmerhelper.ToJimmerEntity
import com.github.llh4github.jimmerhelper.ToJimmerEntityField
import com.github.llh4github.jimmerhelper.example.model.Book

/**
 *
 *
 * Created At 2023/6/18 10:32
 * @author llh
 */
@ToJimmerEntity(Book::class)
data class BookAddVo(
    val id: Int,
    val updatedBy: Int?,
    val storeId: Int?,
    @ToJimmerEntityField(rename = "name")
    val title: String,
)
