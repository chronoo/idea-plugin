package fixs

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.util.createSmartPointer

class AddClassAuthorFix(docComment: PsiDocComment) : LocalQuickFix {
    private val docCommentPointer = docComment.createSmartPointer()

    override fun getFamilyName(): String = "Add author"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val docComment = JavaPsiFacade.getElementFactory(docCommentPointer.project).createDocTagFromText("@author me")
        val children = docCommentPointer.element?.children
        if (children != null) {
            docCommentPointer.element?.addBefore(docComment, children[children.size - 3])
        }
    }
}