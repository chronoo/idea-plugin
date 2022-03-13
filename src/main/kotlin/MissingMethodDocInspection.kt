import com.intellij.codeInsight.intention.AddAnnotationFix
import com.intellij.codeInspection.*
import com.intellij.psi.PsiMethod
import common.STEP_ANNOTATION
import common.isPublic
import common.isTestMethod
import fixs.AddJavaDocFix

class MissingMethodDocInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun checkMethod(
        method: PsiMethod,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor>? = with(ProblemsHolder(manager, method.containingFile, isOnTheFly)) {
        if (!method.isTestMethod && method.isPublic && method.docComment == null && !method.isStepMethod) {
            registerProblem(
                method,
                "Отсутствует документация у метода",
                ProblemHighlightType.WARNING,
                AddJavaDocFix(method),
                AddAnnotationFix(STEP_ANNOTATION, method)
            )
        }
        resultsArray
    }
}