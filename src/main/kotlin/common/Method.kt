package common

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier

val PsiMethod.isPublic
    get() = modifierList.hasModifierProperty(PsiModifier.PUBLIC)