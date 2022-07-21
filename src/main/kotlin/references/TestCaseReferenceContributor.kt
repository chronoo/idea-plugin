package references

import com.intellij.openapi.paths.WebReference
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiFile
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiJavaFileImpl
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import common.className
import common.inTestFile

class TestCaseReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiLiteralExpression::class.java)
                .inFile(psiFile(PsiJavaFileImpl::class.java))
                .with(TestCaseTitleCondition()),

            TestCaseReferenceProvider()
        )
    }
}

class TestCaseTitleCondition : PatternCondition<PsiLiteralExpression>("test_case_title") {
    override fun accepts(literal: PsiLiteralExpression, context: ProcessingContext?): Boolean =
        literal.inTestFile && literal.parentOfType<PsiAnnotation>()?.className == "DisplayName"
}

class TestCaseReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> =
        arrayOf(WebReference(element, "https://www.localhost.ru?test=${element.text}"))
}