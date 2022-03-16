@file:Suppress("UnstableApiUsage")

package commit

import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.ui.RefreshableOnComponent
import com.intellij.util.ui.UIUtil
import javax.swing.JCheckBox

class PropertiesPreCommitHandler(private val panel: CheckinProjectPanel) : CheckinHandler() {
    override fun beforeCheckin(): ReturnResult {
        val hasPropertiesChanges = panel.selectedChanges
            .firstOrNull {
                it.virtualFile?.name?.endsWith(".properties") == true
            }
        return if (hasPropertiesChanges == null) ReturnResult.COMMIT else {
            val yesNoDialogResult = Messages.showYesNoDialog(
                "Вы уверены, что желаете внести изменения пропертей в репозиторий?",
                "Проперти изменены",
                UIUtil.getWarningIcon()
            )

            if (yesNoDialogResult == Messages.YES) ReturnResult.COMMIT else ReturnResult.CANCEL
        }
    }

    override fun getBeforeCheckinConfigurationPanel(): RefreshableOnComponent = PropertiesPreCommitOnComponent()

    companion object {
        val checkBox = JCheckBox("Проверить изменения в *.properties-файлах")
    }
}