import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*
import com.intellij.psi.util.parentOfType
import common.STEP_ANNOTATION
import common.isTestClass

class NotStepActionsInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression?) {
                if (expression?.parentOfType<PsiClass>()?.isTestClass == true) {
                    val method = expression.resolveMethod()
                    if (method != null) {
                        if (!AnnotationUtil.isAnnotated(method, STEP_ANNOTATION, 0)) {
                            holder.registerProblem(
                                expression,
                                "Вызов не @Step-метод в тесте",
                                ProblemHighlightType.WARNING
                            )
                        }
                    }
                }
            }
        }
}