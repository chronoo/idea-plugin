import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.properties.PropertiesInspectionBase
import com.intellij.openapi.vcs.changes.ChangeListManagerImpl
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile

@Deprecated("don't recommend it")
class NotAllowedPropertiesInspection : PropertiesInspectionBase() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : PsiElementVisitor() {
            override fun visitFile(file: PsiFile) {
                val currentPath = file.virtualFile.path
                Thread.sleep(100)
                val propsInDefaultChangeList = ChangeListManagerImpl.getInstance(file.project)
                    .defaultChangeList.changes
                    .filter { currentPath == it.virtualFile?.path }
                if (propsInDefaultChangeList.isNotEmpty()) {
                    holder.registerProblem(
                        file,
                        "Конфигурационные файлы с настройками проекта в основном списке изменений",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
}