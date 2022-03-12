import com.intellij.codeInspection.*
import com.intellij.psi.*
import common.isAssertMethod

class AssertMessageDescriptionInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression?) {
                expression?.apply {
                    val resolvedMethod = this.resolveMethod()
                    if (resolvedMethod != null && resolvedMethod.isAssertMethod) {
                        val parameterList = resolvedMethod.parameterList
                        if (!parameterList.isEmpty && parameterList.firstChild is PsiParameter) {
                            if ((parameterList.firstChild as PsiParameter).name != "message") {
                                holder.registerProblem(
                                    this,
                                    "В Assert-методе не указано сообщение при ошибке",
                                    ProblemHighlightType.WARNING
                                )
                            }
                        }
                    }
                }
            }
        }
}