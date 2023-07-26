package io.github.llh4github.app.dto

import io.github.llh4github.app.entity.Pet
import io.github.llh4github.list.ToJimmerEntity
import io.github.llh4github.list.ToJimmerEntityField

/**
 *
 *
 * Created At 2023/6/17 21:00
 * @author llh
 */
@ToJimmerEntity(Pet::class)
data class PetAddDto(
    val id: Long?,

    @ToJimmerEntityField(rename = "name")
    val petName: String,

)

@ToJimmerEntity(Pet::class)
data class PetUpdateDto(
    @ToJimmerEntityField(ignore = true)
    val id: Long?,
    val age: Int,
    @ToJimmerEntityField(rename = "name")
    val petName: String,
)
