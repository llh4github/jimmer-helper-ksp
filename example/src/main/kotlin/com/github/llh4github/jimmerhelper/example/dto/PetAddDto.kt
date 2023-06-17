package com.github.llh4github.jimmerhelper.example.dto

import com.github.llh4github.jimmerhelper.ToJimmerEntity
import com.github.llh4github.jimmerhelper.example.entity.Pet

/**
 *
 *
 * Created At 2023/6/17 21:00
 * @author llh
 */
@ToJimmerEntity(Pet::class)
data class PetAddDto(
    val name: String
)
