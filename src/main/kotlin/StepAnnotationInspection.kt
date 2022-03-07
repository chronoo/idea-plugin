import StepMethodAnnotationInspection.Companion.STEP_ANNOTATION
import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.jvm.annotation.JvmAnnotationConstantValue
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethod

/*
TODO:   лишние (не хватающие) переносы строк
        лишние изменения в property-файлах
        JavaDoc
        * дубликаты в SQL-запросах
        enum'ы с одинаковыми параметрами
        колличество коммитов в ПРе (не больше одного)
        обязательные аннотации для ALM
        чтобы номера тест-кейсов совпадали с названием класса
        * разные описания в рамках одной фичи
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
                            val method = annotation.parent.parent as PsiMethod
                            val params: String =
                                (annotation.attributes.first().attributeValue as JvmAnnotationConstantValue).constantValue as String
                            val notExistParams = method.parameters.filter { !params.contains("{${it.name!!}}") }.map { it.name }
                            if (notExistParams.isNotEmpty()) {
                                holder.registerProblem(
                                    this,
                                    "В аннотации @Step не указаны параметры $notExistParams",
                                    ProblemHighlightType.WEAK_WARNING
                                )
                            }
                        }

                    }
                }
            }
        }
}