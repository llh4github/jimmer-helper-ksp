package com.github.llh4github.jimmerhelper.core.extract

import com.github.llh4github.jimmerhelper.core.common.*
import com.github.llh4github.jimmerhelper.core.dto.ClassInfoDto
import com.google.devtools.ksp.getAllSuperTypes
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


private fun extractSingleClassInfo(classDeclaration: KSClassDeclaration): ClassInfoDto {
    val isSupperClass = isSupperClass(classDeclaration)
    val fields = extractFieldInfo(classDeclaration.getAllProperties())
    val dto = ClassInfoDto(
        packageName = classDeclaration.packageName.asString(),
        className = classDeclaration.className().simpleName,
        doc = classDeclaration.docString,
        isSupperClass = isSupperClass,
        fields = fields,
        parentNames = extractParentNames(classDeclaration)
    )
    if (isSupperClass)
        SUPER_CLASS_MAP[dto.className] = dto
    return dto
}

private fun extractParentNames(classDeclaration: KSClassDeclaration): List<String> {
    return classDeclaration.getAllSuperTypes()
        .map { it.declaration.simpleName.asString() }
        .filter { it != "Any" }
        .toList()
}

private fun isSupperClass(declaration: KSClassDeclaration): Boolean {
    return declaration.annotations.hasAnno(JimmerAnno.superclass)
}