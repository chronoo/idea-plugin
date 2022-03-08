import com.intellij.codeInspection.*
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import common.isExecuteMethod
import common.isQueryClass
import common.sqlQuery
import common.sqlQueryVar

class DuplicationQueryInspection : AbstractBaseJavaLocalInspectionTool() {
    override fun checkMethod(
        method: PsiMethod,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor>? = with(ProblemsHolder(manager, method.containingFile, isOnTheFly)) {
        if (method.isExecuteMethod && method.containingClass?.isQueryClass == true) {
            val queryVar = method.sqlQueryVar
            if (queryVar != null) {
                val list = method.containingFile.containingDirectory.files
                    .filterNot { it == method.containingFile }
                    .filterIsInstance(PsiJavaFile::class.java)
                    .filter { it.classes.count() == 1 }
                    .mapNotNull { it.classes[0] }
                    .mapNotNull { queryClass -> queryClass.methods.firstOrNull { it.isExecuteMethod } }
                    .filter { it.sqlQuery == queryVar.value }

                if (list.isNotEmpty()) {
                    registerProblem(
                        queryVar,
                        "Запрос встречается в ${list.map { it.containingClass?.name }}",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
        resultsArray
    }
}