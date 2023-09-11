package io.github.llh4github.jimmer.ksp.parser

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.llh4github.core.ToJimmerEntity
import io.github.llh4github.jimmer.ksp.common.JimmerAnno
import io.github.llh4github.jimmer.ksp.common.JimmerEntityAnno
import io.github.llh4github.jimmer.ksp.common.hasAnno
import io.github.llh4github.jimmer.ksp.common.hasAnyAnno
import io.github.llh4github.jimmer.ksp.dto.ClassDefinition
import org.babyfish.jimmer.ksp.className

/**
 *
 *
 * Created At 2023/9/11 16:32
 * @author llh
 */
class ClassDefinitionParser(
    private val sequence: Sequence<KSClassDeclaration>
) {
    fun parse(): List<ClassDefinition> {
        return sequence.map { parseClass(it) }
            .toList()
    }

    private fun parseClass(declaration: KSClassDeclaration): ClassDefinition {
        return ClassDefinition(
            name = declaration.className().simpleName,
            packageName = declaration.packageName.asString(),
            fields = FieldDefinitionParser(declaration).parse(),
            parentNames = extractParentNames(declaration),
            isHelperDtoClass = declaration.annotations.hasAnno(ToJimmerEntity::class),
            isJimmerModel = declaration.annotations.hasAnyAnno(JimmerEntityAnno),
            isMappedSuperclass = declaration.annotations.hasAnno(JimmerAnno.superclass),
        )
    }

    private fun extractParentNames(classDeclaration: KSClassDeclaration): List<String> {
        return classDeclaration.getAllSuperTypes()
            .map { it.declaration.simpleName.asString() }
            .filter { it != "Any" }
            .toList()
    }
}