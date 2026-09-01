package dev.gaphunter.enumordinalpersistencecompanion.model

import com.intellij.psi.PsiElement

/** One `@Enumerated` field on a real JPA `@Entity` class with no explicit `EnumType.STRING` -- persists as ORDINAL by default. */
data class OrdinalPersistenceHit(val anchor: PsiElement, val fieldName: String)
