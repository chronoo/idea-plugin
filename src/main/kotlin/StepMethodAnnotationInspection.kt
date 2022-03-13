import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInsight.intention.AddAnnotationFix
import com.intellij.codeInspection.*
import com.intellij.psi.PsiMethod
import common.STEP_ANNOTATION
import common.isPublic
import common.isStepsClass

class StepMethodAnnotationInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun checkMethod(
        method: PsiMethod,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor>? = with(ProblemsHolder(manager, method.containingFile, isOnTheFly)) {
        if (method.isPublic && method.containingClass?.isStepsClass == true) {
            if (method.isStepMethod) {
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
}

val PsiMethod.isStepMethod
    get() = AnnotationUtil.isAnnotated(this, STEP_ANNOTATION, 0)