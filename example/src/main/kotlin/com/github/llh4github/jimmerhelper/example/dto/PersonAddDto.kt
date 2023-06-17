package com.github.llh4github.jimmerhelper.example.dto

import com.github.llh4github.jimmerhelper.ToJimmerEntity
import com.github.llh4github.jimmerhelper.ToJimmerEntityField
import com.github.llh4github.jimmerhelper.example.entity.Person


@ToJimmerEntity(Person::class, ignoreFields = ["a", "b", "c"])
data class PersonUpdateDto(
    override val id: Long,
    val name: String,
    val age: Int?,
) : BaseDto

@ToJimmerEntity(Person::class, ignoreFields = ["a", "b", "c"])
data class PersonAddDto(
    val name: String,
    val age: Int?,
//    @ToJimmerEntityField(ignore = true, rename = "another")
//    val other: String,
)