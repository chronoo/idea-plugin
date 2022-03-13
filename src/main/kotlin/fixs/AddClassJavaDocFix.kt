package fixs

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.util.createSmartPointer

class AddClassJavaDocFix(aClass: PsiClass) : LocalQuickFix {
    private val classPointer = aClass.createSmartPointer()

    override fun getFamilyName(): String = "Add javaDoc"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        classPointer.element?.let { clazz ->
            val javaDoc = """
            /**
             * ${clazz.name}
             *
             * @author me
            */
        """.trimIndent()
            val docComment = JavaPsiFacade.getElementFactory(project).createDocCommentFromText(javaDoc)
            clazz.firstChild.addBefore(docComment, clazz.firstChild)
        }
    }
}