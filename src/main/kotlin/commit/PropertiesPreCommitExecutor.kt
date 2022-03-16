package commit

import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory

class PropertiesPreCommitExecutor : CheckinHandlerFactory() {
    override fun createHandler(panel: CheckinProjectPanel, commitContext: CommitContext): CheckinHandler =
        PropertiesPreCommitHandler(panel)

    companion object {
        const val KEY = "PROPERTIES_PRE_COMMIT_CHECK_EXECUTOR_KEY"
    }
}

