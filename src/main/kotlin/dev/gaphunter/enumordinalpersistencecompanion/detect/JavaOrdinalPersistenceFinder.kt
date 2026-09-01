package dev.gaphunter.enumordinalpersistencecompanion.detect

import com.intellij.psi.JavaRecursiveElementWalkingVisitor
import com.intellij.psi.PsiAnnotationMemberValue
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiReferenceExpression
import dev.gaphunter.enumordinalpersistencecompanion.model.OrdinalPersistenceHit

/**
 * Finds a field annotated `@Enumerated` (JPA) with no explicit
 * `EnumType.STRING` (JPA's own default is `ORDINAL`), on a class that
 * is a real JPA `@Entity` -- confirming the field genuinely reaches
 * persistent storage, not just an annotation in isolation. Reordering
 * or inserting a value in the middle of the enum after data has
 * already been persisted with `ORDINAL` silently corrupts existing
 * records, since the stored ordinal no longer corresponds to the same
 * constant.
 *
 * **v0.1 scope, stated honestly:** only standard JPA `@Enumerated` on
 * a class annotated `@Entity` -- doesn't cover custom serialization
 * (Jackson `@JsonValue`, protobuf enum) or non-JPA persistence
 * frameworks.
 */
object JavaOrdinalPersistenceFinder {

    fun findAll(file: PsiFile): List<OrdinalPersistenceHit> {
        val hits = mutableListOf<OrdinalPersistenceHit>()
        file.accept(object : JavaRecursiveElementWalkingVisitor() {
            override fun visitClass(psiClass: PsiClass) {
                super.visitClass(psiClass)
                if (!hasAnnotation(psiClass, "Entity")) return
                for (field in psiClass.fields) {
                    hitFor(field)?.let { hits += it }
                }
            }
        })
        return hits
    }

    private fun hitFor(field: PsiField): OrdinalPersistenceHit? {
        val enumeratedAnnotation = field.annotations.firstOrNull { annotation ->
            annotation.nameReferenceElement?.referenceName == "Enumerated"
        } ?: return null

        val valueArg = enumeratedAnnotation.findAttributeValue("value")
        if (isExplicitlyString(valueArg)) return null // already the safe form -- nothing to flag

        return OrdinalPersistenceHit(field.nameIdentifier ?: field, field.name)
    }

    /** True when the `@Enumerated` value argument explicitly names `EnumType.STRING` (a qualified enum constant reference). */
    private fun isExplicitlyString(valueArg: PsiAnnotationMemberValue?): Boolean {
        val referenceName = (valueArg as? PsiReferenceExpression)?.referenceName
        if (referenceName == "STRING") return true
        // Defensive: also accept a plain literal "STRING" text, though JPA's
        // real annotation value is always the enum constant form above.
        return (valueArg as? PsiLiteralExpression)?.value == "STRING"
    }

    private fun hasAnnotation(psiClass: PsiClass, simpleName: String): Boolean =
        psiClass.annotations.any { it.nameReferenceElement?.referenceName == simpleName }
}
