package com.github.llh4github.jimmerhelper.example.dto

import com.github.llh4github.jimmerhelper.ToJimmerEntity
import com.github.llh4github.jimmerhelper.ToJimmerEntityField
import com.github.llh4github.jimmerhelper.example.entity.Person

/**
 *
 *
 * Created At 2023/6/15 17:23
 * @author llh
 */
@ToJimmerEntity(Person::class, ignoreFields = ["a", "b", "c"])
data class PersonAddDto(
    override val id: Int,
    val name: String,
    val age: Int?,
    @ToJimmerEntityField(ignore = true, rename = "another")
    val other: String,
) : BaseDto


