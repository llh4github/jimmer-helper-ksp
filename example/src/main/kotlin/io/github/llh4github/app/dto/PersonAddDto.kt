package io.github.llh4github.app.dto


import io.github.llh4github.app.entity.Person
import io.github.llh4github.core.ToJimmerEntity
import io.github.llh4github.core.ToJimmerEntityField


@ToJimmerEntity(Person::class, ignoreFields = ["a", "b", "c", "pets"])
data class PersonUpdateDto(
    override val id: Long,
    val name: String,
    val age: Int?,

    @ToJimmerEntityField(ignore = true, rename = "dog")
    val pets: List<String>,
) : BaseDto

@ToJimmerEntity(Person::class, ignoreFields = ["other", "b", "c"])
data class PersonAddDto(
    val name: String,
    val age: Int?,
    @ToJimmerEntityField(ignore = true, rename = "another")
    val other: String,
)