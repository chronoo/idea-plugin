import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInsight.intention.AddAnnotationFix
import com.intellij.codeInspection.*
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier

class StepMethodAnnotationInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun checkMethod(
        method: PsiMethod,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor>? = with(ProblemsHolder(manager, method.containingFile, isOnTheFly)) {
        if (method.isPublic && method.containingClass?.name?.endsWith("Steps") == true) {
            if (!AnnotationUtil.isAnnotated(method, STEP_ANNOTATION, 0)) {
                method.parameterList
                registerProblem(
                    method,
                    "Нет аннотации @Step над step-методом",
                    ProblemHighlightType.WARNING,
                    AddAnnotationFix(STEP_ANNOTATION, method)
                )
            }
        }
        resultsArray
    }

    companion object {
        const val STEP_ANNOTATION = "io.qameta.allure.Step"
    }
}

val PsiMethod.isPublic
    get() = modifierList.hasModifierProperty(PsiModifier.PUBLIC)