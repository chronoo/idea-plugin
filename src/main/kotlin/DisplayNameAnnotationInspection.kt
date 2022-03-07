import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*
import common.DISPLAY_NAME_ANNOTATION
import common.containingClass
import common.textValue

class DisplayNameAnnotationInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitAnnotation(annotation: PsiAnnotation?) {
                fun PsiMethod.isDisplayNamed(): Boolean = hasAnnotation(DISPLAY_NAME_ANNOTATION)

                annotation?.apply {
                    if (hasQualifiedName(DISPLAY_NAME_ANNOTATION)) {
                        val annotations = annotation.containingFile.containingDirectory.files
                            .filterIsInstance(PsiJavaFile::class.java)
                            .filter { it.classes.count() == 1 }
                            .mapNotNull { it.classes[0] }
                            .mapNotNull { it.methods.first(PsiMethod::isDisplayNamed) }
                            .mapNotNull { it.getAnnotation(DISPLAY_NAME_ANNOTATION) }
                            .filter { it != this }
                            .filter { it.textValue == this.textValue }
                        if (annotations.isNotEmpty()) {
                            holder.registerProblem(
                                this,
                                "Описание тест-кейса в аннотации @DisplayName неуникально ${annotations.map { it.containingClass }}",
                                ProblemHighlightType.GENERIC_ERROR
                            )
                        }
                    }
                }
            }
        }
}
