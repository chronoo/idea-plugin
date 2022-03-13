import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementVisitor
import common.isCommentData
import common.isTestClass
import fixs.AddClassAuthorFix
import fixs.AddClassDescriptionFix
import fixs.AddClassJavaDocFix

class MissingJavaDocInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitClass(aClass: PsiClass?) {
                aClass?.apply {
                    if (!isTestClass) {
                        if (docComment == null) {
                            holder.registerProblem(
                                aClass,
                                "У класса нет документации",
                                ProblemHighlightType.WARNING,
                                AddClassJavaDocFix(aClass)
                            )
                        } else {
                            val authorTag = docComment?.findTagByName("author")
                            if (authorTag == null) {
                                holder.registerProblem(
                                    docComment!!,
                                    "В документации нет тега @author",
                                    ProblemHighlightType.WARNING,
                                    AddClassAuthorFix(docComment!!)
                                )
                            }
                            val comment = docComment?.children?.firstOrNull { it.isCommentData }
                            if (comment != null && comment.text.isNullOrBlank()) {
                                holder.registerProblem(
                                    docComment!!,
                                    "В документации нет описания класса",
                                    ProblemHighlightType.WARNING,
                                    AddClassDescriptionFix(docComment!!)
                                )
                            }
                        }
                    }
                }
            }
        }
}
