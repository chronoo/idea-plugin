package common

import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod

val PsiAnnotation.textValue: String?
    get() = parameterList.attributes
        .filter { it.value is PsiLiteralExpression }
        .firstNotNullOfOrNull { it.value as PsiLiteralExpression }
        ?.value as String?

val PsiAnnotation.containingClass: String?
    get() = (parent.parent.parent as PsiClass).name

val PsiAnnotation.containingMethod: PsiMethod
    get() = parent.parent as PsiMethod