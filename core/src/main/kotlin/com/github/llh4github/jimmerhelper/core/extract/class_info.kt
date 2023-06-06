package com.github.llh4github.jimmerhelper.core.extract

import com.github.llh4github.jimmerhelper.core.common.*
import com.github.llh4github.jimmerhelper.core.dto.ClassInfoDto
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import org.babyfish.jimmer.ksp.className

fun extractClassInfo(sequence: Sequence<KSFile>): List<ClassInfoDto> {
    return sequence
        .flatMap { it.declarations.filterIsInstance<KSClassDeclaration>() }
        .filter { isJimmerModelClass(it) }
        .map { extractSingleClassInfo(it) }
        .toList()
}


private fun isJimmerModelClass(declaration: KSClassDeclaration): Boolean {
    return declaration.annotations.hasAnyAnno(JimmerEntityAnno)
}


private fun extractSingleClassInfo(declaration: KSClassDeclaration): ClassInfoDto {
    val dto = ClassInfoDto(
        packageName = declaration.packageName.asString(),
        className = declaration.className().simpleName,
        doc = declaration.docString,
        isSupperClass = isSupperClass(declaration),
    )
    logger.info("test  : $dto")
    return dto
}
private fun isSupperClass(declaration: KSClassDeclaration): Boolean {
   return declaration.annotations.hasAnno(JimmerAnno.superclass)
}