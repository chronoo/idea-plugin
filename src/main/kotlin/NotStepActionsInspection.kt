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
import common.*

class NotStepActionsInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression?) {
                if (expression?.parentOfType<PsiClass>()?.isTestClass == true) {
                    if (expression.parentsOfType<PsiMethod>().firstOrNull()?.isTestMethod == true) {
                        if (expression is CompositeElement) {
                            expression.resolveMethod()?.let { method ->
                                if (method.byJava || method.isBuilder) return
                                if (!AnnotationUtil.isAnnotated(method, ALLOWED_ANNOTATIONS, 0)) {
                                    val children = expression.findChildByType(ElementType.REFERENCE_EXPRESSION)
                                    if (children is PsiReferenceExpression) {
                                        val parentsOfType = expression.parentsOfType<PsiMethodCallExpression>()
                                        val first = parentsOfType.firstOrNull()?.getChildRecursive()
                                        setOf(
                                            when (first) {
                                                is PsiMethodCallExpression, null -> first
                                                else -> first.parentOfType<PsiMethodCallExpression>()
                                            }?.identifier,

                                            parentsOfType.lastOrNull()?.identifier
                                        ).filterNotNull()
                                            .filter { it.text == method.name }
                                            .forEach {
                                                holder.registerProblem(
                                                    it,
                                                    "?????????? ???? @Step-???????????? ?? ??????????",
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

    private val PsiMethod.byJava
        get() = containingClass?.qualifiedName?.startsWith("java.") ?: false

    private val PsiMethod.isBuilder
        get() = containingClass?.name?.endsWith("Builder", ignoreCase = true) ?: false

    private val PsiElement.identifier
        get() = children.firstOrNull()?.children?.lastOrNull { it.elementType == ElementType.IDENTIFIER }

    private fun PsiElement.getChildRecursive(): PsiElement {
        return when (firstChild) {
            is PsiMethodCallExpression -> firstChild.getChildRecursive()
            is PsiReferenceExpression -> firstChild.getChildRecursive()
            else -> this
        }
    }

    companion object {
        val ALLOWED_ANNOTATIONS = setOf(STEP_ANNOTATION, INVISIBLE_STEP_ANNOTATION)
    }
}
