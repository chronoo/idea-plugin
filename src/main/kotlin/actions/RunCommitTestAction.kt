package actions

import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.icons.AllIcons.Actions.Execute
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.util.containers.ContainerUtil
import com.intellij.vcs.log.VcsLogDataKeys
import com.intellij.vcs.log.util.VcsLogUtil

class RunCommitTestAction : DumbAwareAction("Test", "Run all test", Execute) {
    override fun actionPerformed(e: AnActionEvent) {
        val log = e.getData(VcsLogDataKeys.VCS_LOG)
        if (log != null) {
            val project = e.project!!
            val changes = log.selectedDetails.firstOrNull()?.changes
            if (changes != null) {

//                val target = file.getCanonicalFile()
//                ProjectView.getInstance(project).select(psiFile, target, false)
//                val settings = RunnerAndConfigurationSettingsImpl(
//                    RunManagerImpl.getInstanceImpl(project),
//                    JUnitConfiguration
//                )
//                builder = ExecutionEnvironmentBuilder.create(DefaultRunExecutor.getRunExecutorInstance(), settings)
//                val action = ActionManager.getInstance().getAction("RunClass")
//                action.
//                ProgramRunnerUtil.executeConfiguration(configuration, DefaultRunExecutor.getRunExecutorInstance())
            }
        }
    }

    override fun isDumbAware(): Boolean = true
}