import TestClassNumberInspection.Companion.TEST_ANNOTATION
import TestClassNumberInspection.Companion.TMS_LINK_ANNOTATION
import com.intellij.codeInsight.intention.AddAnnotationFix
import com.intellij.codeInspection.*
import com.intellij.psi.PsiMethod

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
                    "Нет аннотации @${annotation.split(".").last()}",
                    ProblemHighlightType.WARNING,
                    AddAnnotationFix(annotation, method)
                )
            }
        }

        if (method.hasAnnotation(TEST_ANNOTATION)) {
            if (method.containingClass?.name?.startsWith("Test_") == true) {
                method.checkAnnotation(TMS_LINK_ANNOTATION)
                method.checkAnnotation(EPIC_ANNOTATION)
                method.checkAnnotation(FEATURE_ANNOTATION)
                method.checkAnnotation(STORY_ANNOTATION)
                method.checkAnnotation(DISPLAY_NAME_ANNOTATION)
            }
        }
        resultsArray
    }

    companion object {
        const val EPIC_ANNOTATION = "io.qameta.allure.Epic"
        const val FEATURE_ANNOTATION = "io.qameta.allure.Feature"
        const val STORY_ANNOTATION = "io.qameta.allure.Story"
        const val DISPLAY_NAME_ANNOTATION = "io.qameta.allure.junit4.DisplayName"
    }
}