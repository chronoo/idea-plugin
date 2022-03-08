package common

import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier

val PsiMethod.isPublic
    get() = modifierList.hasModifierProperty(PsiModifier.PUBLIC)

val PsiMethod.sqlQueryVar
    get() = body?.statements
        ?.mapNotNull { it.children.firstOrNull() }
        ?.filterIsInstance(PsiLocalVariable::class.java)
        ?.firstOrNull { it.name == "query" }
        ?.children
        ?.filterIsInstance(PsiLiteralExpression::class.java)
        ?.firstOrNull()

val PsiMethod.sqlQuery
    get() = sqlQueryVar?.value as String?

val PsiMethod.isExecuteMethod
    get() = name.contains("execute")
