package io.github.llh4github.jimmer.ksp.generator

import com.squareup.kotlinpoet.FileSpec
import io.github.llh4github.jimmer.ksp.dto.ClassDefinition

/**
 *
 *
 * Created At 2023/9/12 10:12
 * @author llh
 */
abstract class AbstractGenerator {
    abstract fun generate(list: List<ClassDefinition>): List<FileSpec>
}