import com.intellij.codeInsight.intention.QuickFixFactory
import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiEnumConstant
import common.equal
import common.params

class EnumElementsDuplicateInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitEnumConstant(enumConstant: PsiEnumConstant?) {
                val params = enumConstant?.params

                if (params != null && params.isNotEmpty()) {
                    val enumConstants = enumConstant.containingClass?.allFields
                        ?.filterIsInstance(PsiEnumConstant::class.java)
                        ?.filterNot { it == enumConstant }
                        ?.filter { enumConstant.equal(it) }
                        ?: listOf()
                    if (enumConstants.isNotEmpty()) {
                        enumConstants.forEach {
                            holder.registerProblem(
                                it,
                                "Дубль элемента ${enumConstant.name}",
                                ProblemHighlightType.GENERIC_ERROR,
                                QuickFixFactory.getInstance().createDeleteFix(it)
                            )
                        }
                    }
                }
            }
        }
}