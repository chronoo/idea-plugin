package push

import com.intellij.dvcs.push.PrePushHandler
import com.intellij.dvcs.push.PrePushHandler.Result
import com.intellij.dvcs.push.PushInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.ui.Messages
import com.intellij.util.ui.UIUtil
import java.util.concurrent.atomic.AtomicReference

class MultipleCommitsPrePushHandler : PrePushHandler {
    override fun getPresentableName(): String = "Smart-Bio push handler"

    override fun handle(pushDetails: MutableList<PushInfo>, indicator: ProgressIndicator): Result {
        val commitCount = pushDetails.firstOrNull()?.commits?.size ?: 0
        return if (commitCount > 1) {
            invokeAndWait(indicator.modalityState) {
                val yesNoDialog = Messages.showYesNoDialog(
                    "Вы пытаетесь запушить больше одного коммита. Вы уверены, что хотите этого?",
                    "Множественные коммиты",
                    UIUtil.getWarningIcon()
                )
                if (yesNoDialog == Messages.YES) {
                    val twiceYesNoDialog = Messages.showYesNoDialog(
                        "Точно уверены?",
                        "Подтверждение",
                        UIUtil.getWarningIcon()
                    )
                    if (twiceYesNoDialog == Messages.YES) {
                        return@invokeAndWait Result.OK
                    }
                }
                Result.ABORT
            }
        }
        else
            Result.OK
    }

    private fun <T> invokeAndWait(modalityState: ModalityState, computable: () -> T): T {
        val ref = AtomicReference<T>()
        ApplicationManager.getApplication().invokeAndWait({ ref.set(computable.invoke()) }, modalityState)
        return ref.get()
    }
}