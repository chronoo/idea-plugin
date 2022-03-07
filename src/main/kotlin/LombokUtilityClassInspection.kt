import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInsight.intention.AddAnnotationFix
import com.intellij.codeInspection.*
import com.intellij.psi.PsiClass
import common.UTILITY_CLASS_ANNOTATION
import common.isStepsClass

class LombokUtilityClassInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun checkClass(
        aClass: PsiClass,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor>? = with(ProblemsHolder(manager, aClass.containingFile, isOnTheFly)) {
        if (aClass.isStepsClass) {
            if (!AnnotationUtil.isAnnotated(aClass, UTILITY_CLASS_ANNOTATION, 0)) {
                registerProblem(
                    aClass,
                    "Нет аннотации @UtilityClass над step-классом",
                    ProblemHighlightType.WARNING,
                    AddAnnotationFix(UTILITY_CLASS_ANNOTATION, aClass)
                )
            }
        }
        resultsArray
    }
}