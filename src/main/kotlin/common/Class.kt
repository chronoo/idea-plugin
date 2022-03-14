package common

import com.intellij.psi.PsiClass

val PsiClass.isTestClass: Boolean
    get() = name?.startsWith("Test_") == true

val PsiClass.isStepsClass: Boolean
    get() = name?.endsWith("Steps") == true

val PsiClass.caseNumber: String?
    get() = name?.split("_")?.get(1)

val PsiClass.isQueryClass: Boolean
    get() = qualifiedName?.startsWith(QUERY_PACKAGE) == true

val PsiClass.isGenerated: Boolean
    get() = qualifiedName?.startsWith("generated.") == true || qualifiedName?.startsWith("org.") == true

const val QUERY_PACKAGE = "ru.sbrf.biometrics.query."