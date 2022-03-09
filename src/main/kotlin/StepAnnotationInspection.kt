import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiElementVisitor
import common.STEP_ANNOTATION
import common.containingMethod
import common.textValue

/*
TODO:   лишние (не хватающие) переносы строк
        JavaDoc
        количество коммитов в ПРе (не больше одного)
        * двойные пробелы
*/
class StepAnnotationInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitAnnotation(annotation: PsiAnnotation?) {
                annotation?.apply {
                    if (hasQualifiedName(STEP_ANNOTATION)) {
                        if (attributes.isEmpty()) {
                            holder.registerProblem(
                                this,
                                "Не заполнено описание в аннотации @Step",
                                ProblemHighlightType.WARNING
                            )
                        } else {
                            val methodParameters = containingMethod.parameterList.parameters
                            val notExistParams = methodParameters
                                .filter { textValue?.contains("\\{${it.name}.*}".toRegex()) != true }
                                .map { it.name }

                            when {
                                methodParameters.isNotEmpty() && methodParameters.count() == notExistParams.count() ->
                                    holder.registerProblem(
                                        this,
                                        "В аннотации @Step не указано ни одного параметра",
                                        ProblemHighlightType.WARNING
                                    )

                                notExistParams.isNotEmpty() -> holder.registerProblem(
                                    this,
                                    "В аннотации @Step не указаны параметры $notExistParams",
                                    ProblemHighlightType.POSSIBLE_PROBLEM
                                )
                            }
                        }
                    }
                }
            }
        }
}