package common

import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiLiteralExpression

val PsiAnnotation.textValue: String
    get() = (parameterList.attributes.first().value as PsiLiteralExpression).value as String

val PsiAnnotation.containedClass: String?
    get() = (parent.parent.parent as PsiClass).name