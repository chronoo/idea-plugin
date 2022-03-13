package fixs

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.util.createSmartPointer
import com.intellij.psi.util.parentOfType

class AddClassDescriptionFix(docComment: PsiElement) : LocalQuickFix {
    private val docCommentPointer = docComment.createSmartPointer()

    override fun getFamilyName(): String = "Add description"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val docComment = docCommentPointer.element
        if (docComment != null) {
            val prefix = "/**\n"
            val description = docComment.parentOfType<PsiClass>()?.name ?: "description"
            val docCommentWithDescription = docComment.text.replaceFirst(prefix, "$prefix * $description\n \n *\n")
            docComment.replace(
                JavaPsiFacade.getElementFactory(docCommentPointer.project)
                    .createDocCommentFromText(docCommentWithDescription)
            )
        }
    }
}