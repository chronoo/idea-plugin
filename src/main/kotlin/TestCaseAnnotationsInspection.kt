import com.intellij.codeInsight.intention.AddAnnotationFix
import com.intellij.codeInspection.*
import com.intellij.psi.PsiMethod
import common.*

class TestCaseAnnotationsInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun checkMethod(
        method: PsiMethod,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor>? = with(ProblemsHolder(manager, method.containingFile, isOnTheFly)) {
        fun PsiMethod.checkAnnotation(annotation: String) {
            if (!hasAnnotation(annotation)) {
                registerProblem(
                    method,
                    "Нет аннотации @${annotation.lastWord}",
                    ProblemHighlightType.WARNING,
                    AddAnnotationFix(annotation, method)
                )
            }
        }

        if (method.isTestMethod) {
            if (method.containingClass?.isTestClass == true) {
                method.checkAnnotation(TMS_LINK_ANNOTATION)
                method.checkAnnotation(EPIC_ANNOTATION)
                method.checkAnnotation(FEATURE_ANNOTATION)
                method.checkAnnotation(STORY_ANNOTATION)
                method.checkAnnotation(DISPLAY_NAME_ANNOTATION)
            }
        }
        resultsArray
    }

    private val String.lastWord
        get() = split(".").last()
}