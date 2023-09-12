package io.github.llh4github.jimmer.ksp.generator

import com.squareup.kotlinpoet.FileSpec
import io.github.llh4github.jimmer.ksp.dto.ClassDefinition


private val generators = listOf(
    BaseClassGen, InputSupportDtoGen,
    SimpleFetcherGen, ToJimmerEntityFunGen
)

/**
 * 外部调用方法
 */
fun generate(list: List<ClassDefinition>): List<FileSpec> {
    return generators.map { it.generate(list) }
        .flatten()
        .toList()
}