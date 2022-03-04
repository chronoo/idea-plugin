import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages

class Acttion : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val editor = event.getData(PlatformDataKeys.EDITOR)!!
        val selectedText = editor.selectionModel.selectedText
        Messages.showInfoMessage(selectedText, "selected")
    }
}