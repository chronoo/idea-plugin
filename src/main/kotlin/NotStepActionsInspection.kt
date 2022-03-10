import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.impl.source.tree.ElementType
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.psi.util.parentsOfType
import common.STEP_ANNOTATION
import common.isTestClass
import common.isTestMethod

class NotStepActionsInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression?) {
                if (expression?.parentOfType<PsiClass>()?.isTestClass == true) {
                    if (expression.parentsOfType<PsiMethod>().firstOrNull()?.isTestMethod == true) {
                        expression.resolveMethod()?.let { method ->
                            if (!AnnotationUtil.isAnnotated(method, STEP_ANNOTATION, 0)) {
                                if (expression is CompositeElement) {
                                    val children = expression.findChildByType(ElementType.REFERENCE_EXPRESSION)
                                    if (children is PsiReferenceExpression) {
                                        val methodsExpressions = expression.parentsOfType<PsiMethodCallExpression>()
                                            .lastOrNull()
                                            ?.children?.firstOrNull()
                                            ?.children?.filter { it.elementType == ElementType.IDENTIFIER }

                                        methodsExpressions?.firstAndLastOrNull()?.forEach { methodExpression ->
                                            holder.registerProblem(
                                                methodExpression,
                                                "Вызов не @Step-метода в тесте",
                                                ProblemHighlightType.WARNING
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun <T> List<T>.firstAndLastOrNull(): List<T>? =
        when {
            isEmpty() -> null
            size == 1 -> listOf(first())
            else -> listOf(first(), last())
        }
}