package fixs

import com.intellij.codeInsight.intention.impl.AddJavadocIntention
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class AddJavaDocFix(element: PsiElement) : LocalQuickFixAndIntentionActionOnPsiElement(element) {
    private val intention = AddJavadocIntention()

    override fun getFamilyName(): String = intention.familyName

    override fun getText(): String = intention.text

    override fun invoke(
        project: Project,
        file: PsiFile,
        editor: Editor?,
        startElement: PsiElement,
        endElement: PsiElement
    ) = intention.invoke(project, editor, startElement)
}