import com.intellij.codeInspection.*
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod

class TestClassNumberInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun checkMethod(
        method: PsiMethod,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor>? = with(ProblemsHolder(manager, method.containingFile, isOnTheFly)) {
        if (method.hasAnnotation(TEST_ANNOTATION)) {
            if (method.containingClass?.name?.startsWith("Test_") == true) {
                if (method.hasAnnotation(TMS_LINK_ANNOTATION)) {
                    val testName: String? = method.containingClass?.name?.split("_")?.get(1)
                    if (testName != null && method.getAnnotation(TMS_LINK_ANNOTATION)
                            ?.parameterList
                            ?.attributes
                            ?.any { (it.value as PsiLiteralExpression).value != testName } == true) {
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

    companion object {
        const val TEST_ANNOTATION = "org.junit.Test"
        const val TMS_LINK_ANNOTATION = "io.qameta.allure.TmsLink"
    }
}