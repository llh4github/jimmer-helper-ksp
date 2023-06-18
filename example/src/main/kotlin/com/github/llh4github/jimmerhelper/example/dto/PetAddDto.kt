package com.github.llh4github.jimmerhelper.example.dto

import com.github.llh4github.jimmerhelper.ToJimmerEntity
import com.github.llh4github.jimmerhelper.ToJimmerEntityField
import com.github.llh4github.jimmerhelper.example.entity.Pet

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
