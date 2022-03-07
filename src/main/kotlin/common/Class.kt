package common

import com.intellij.psi.PsiClass

val PsiClass.isTestClass: Boolean
    get() = name?.startsWith("Test_") == true

val PsiClass.caseNumber: String?
    get() = name?.split("_")?.get(1)