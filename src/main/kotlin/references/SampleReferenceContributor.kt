package references

import com.intellij.json.psi.JsonFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.TextRange.allOf
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiFile
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiJavaFileImpl
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import common.inTestFile

class SampleReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiLiteralExpression::class.java).inFile(psiFile(PsiJavaFileImpl::class.java))
                .with(object : PatternCondition<PsiLiteralExpression?>("meth") {
                    override fun accepts(literal: PsiLiteralExpression, context: ProcessingContext?): Boolean {
                        if (!literal.inTestFile || literal.parentOfType<PsiAnnotationParameterList>() != null) return false
                        return literal.parentOfType<PsiDeclarationStatement>() != null || literal.parentOfType<PsiExpressionList>() != null
                    }
                }), SampleReferenceProvider()
        )
    }
}

class SampleReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> =
        arrayOf(SampleReference(element))
}

class SampleReference(private val psiElement: PsiElement) :
    PsiReferenceBase<PsiElement>(psiElement, allOf(psiElement.text)) {
    override fun resolve(): PsiElement? {
        val project = psiElement.project
        val virtualFile = project.root?.findFileByRelativePath("src/test/resources/data/samples.json") ?: return null
        val jsonFile = PsiManager.getInstance(project).findFile(virtualFile) as JsonFile? ?: return null
        return jsonFile.children.first().children.firstOrNull { it.text.contains(""""name": ${psiElement.text}""") }
    }

    override fun getRangeInElement(): TextRange = TextRange(1, psiElement.textLength - 1)
}

val Project.root: VirtualFile?
    get() = ProjectRootManager.getInstance(this).contentRoots.firstOrNull()