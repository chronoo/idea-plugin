import com.intellij.codeInspection.*
import com.intellij.psi.PsiMethod
import common.*

class TestClassNumberInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun checkMethod(
        method: PsiMethod,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor>? = with(ProblemsHolder(manager, method.containingFile, isOnTheFly)) {
        if (method.isTestMethod) {
            if (method.containingClass?.isTestClass == true) {
                if (method.hasAnnotation(TMS_LINK_ANNOTATION)) {
                    val caseNumber = method.containingClass?.caseNumber
                    if (caseNumber != null && method.getAnnotation(TMS_LINK_ANNOTATION)?.textValue != caseNumber) {
                        registerProblem(
                            method.containingClass!!,
                            "Номер тест-кейса в названии класса не совпадает с номером в аннотации @TmsLink",
                            ProblemHighlightType.GENERIC_ERROR
                        )
                    }
                }
            }
        }
        resultsArray
    }
}
