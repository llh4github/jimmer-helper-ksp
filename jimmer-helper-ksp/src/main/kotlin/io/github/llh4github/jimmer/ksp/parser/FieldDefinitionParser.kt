package io.github.llh4github.jimmer.ksp.parser

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import io.github.llh4github.core.ToJimmerEntityField
import io.github.llh4github.jimmer.ksp.common.*
import io.github.llh4github.jimmer.ksp.dto.FieldDefinition
import io.github.llh4github.jimmer.ksp.dto.HelperFieldRestrict
import io.github.llh4github.jimmer.ksp.dto.JimmerFieldRestrict
import io.github.llh4github.jimmer.ksp.dto.TypeInfo

/**
 *
 *
 * Created At 2023/9/11 13:31
 * @author llh
 */
class FieldDefinitionParser(
    private val definition: KSClassDeclaration
) {

    fun parse(): List<FieldDefinition> {
        return definition.getAllProperties()
            .map { parseFieldInfo(it) }
            .toList()
    }

    private fun parseFieldInfo(property: KSPropertyDeclaration): FieldDefinition {
        val args = property.type.resolve().arguments
        var genericParam: TypeInfo? = null
        if (args.isNotEmpty()) {
            // 只考虑单个泛型参数
            args[0].let {
                val typePackage = it.type!!.resolve().declaration.packageName.asString()
                genericParam = TypeInfo(
                    typeName = it.typeName().toString(),
                    typePackage = typePackage
                )
            }
        }

        val typeInfo = TypeInfo(
            typeName = property.type.resolve().declaration.simpleName.asString(),
            typePackage = property.type.resolve().declaration.packageName.asString(),
        )

        return FieldDefinition(
            name = property.simpleName.asString(),
            doc = property.docString,
            typeInfo = typeInfo,
            genericParam = genericParam,
            nullable = property.type.resolve().nullability == Nullability.NULLABLE,
            jimmerFieldRestrict = parseJimmerFieldRestrict(property),
            helperFieldRestrict = parseHelperFieldRestrict(property),
        )
    }

    private fun parseJimmerFieldRestrict(property: KSPropertyDeclaration): JimmerFieldRestrict {
        val isJimmerKey = property.annotations.hasAnno(JimmerAnno.key)
        val isJimmerId = property.annotations.hasAnno(JimmerAnno.id)
        val isRelation = property.annotations.hasAnyAnno(relationAnnoList)
        val isIdViewListFlag = property.annotations.hasAnno(JimmerAnno.idView)
        return JimmerFieldRestrict(
            isJimmerKey = isJimmerKey,
            isPrimaryKey = isJimmerId,
            isRelation = isRelation,
            isIdViewListField = isIdViewListFlag
        )
    }

    private fun parseHelperFieldRestrict(property: KSPropertyDeclaration): HelperFieldRestrict {
        var rename = property.simpleName.asString()
        var ignore = false
        property.annotations.filter {
            val annoClassName = it.annotationType.toTypeName()
            annoClassName == ToJimmerEntityField::class.asClassName()
        }.forEach {
            ignore = it.arguments.getValue(ToJimmerEntityFieldProperties.ignore) as Boolean
            val name = it.arguments.getValue(ToJimmerEntityFieldProperties.rename) as String
            if (name.isNotEmpty()) {
                rename = name
            }
        }

        return HelperFieldRestrict(rename, ignore)
    }
}