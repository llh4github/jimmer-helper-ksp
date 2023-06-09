package com.github.llh4github.jimmerhelper.ksp.generator

import com.github.llh4github.jimmerhelper.ksp.dto.ClassInfoDto
import com.squareup.kotlinpoet.FileSpec

/**
 *
 * Created At 2023/6/7 19:51
 * @author llh
 */
class SuperInterfaceGen(private val dto: ClassInfoDto) {

    private val typeSpec = interfaceBuilder(dto)

    fun build(): FileSpec {

        return FileSpec.builder(dto.inputDtoPkg, dto.inputDtoClassName)
            .addType(
                typeSpec
                    .addKdoc(comment)
                    .addAnnotation(suppressWarns)
                    .addProperties(properties(dto.fields))
                    .build()
            )
            .build()
    }
}