package com.github.llh4github.jimmerhelper.example.dto

import com.github.llh4github.jimmerhelper.ToJimmerEntity
import com.github.llh4github.jimmerhelper.example.entity.Person
import com.github.llh4github.jimmerhelper.example.entity.by
import org.babyfish.jimmer.kt.new

/**
 *
 *
 * Created At 2023/6/15 17:23
 * @author llh
 */
@ToJimmerEntity(Person::class)
data class PersonAddDto(
    val name: String
)


