package com.github.llh4github.jimmerhelper.core.generator

import com.github.llh4github.jimmerhelper.core.common.logger
import com.github.llh4github.jimmerhelper.core.dto.ClassInfoDto
import com.squareup.kotlinpoet.AnnotationSpec
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
            .addType(typeSpec
                .addKdoc(comment)
                .addAnnotation(
                    AnnotationSpec.builder(Suppress::class)
                        .apply {
                            addMember("\"RedundantVisibilityModifier\"")
                            addMember("\"Unused\"")
                        }
                        .build()

                )
                .addProperties(properties(dto.fields))
                .build())
            .build()
    }
}