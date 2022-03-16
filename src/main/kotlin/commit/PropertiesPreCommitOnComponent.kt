package commit

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.vcs.ui.RefreshableOnComponent
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class PropertiesPreCommitOnComponent : RefreshableOnComponent {
    override fun saveState() {
        PropertiesComponent.getInstance().setValue(PropertiesPreCommitExecutor.KEY, PropertiesPreCommitHandler.checkBox.isSelected)
    }

    override fun restoreState() {
        PropertiesPreCommitHandler.checkBox.isSelected = PropertiesComponent.getInstance().getBoolean(PropertiesPreCommitExecutor.KEY)
    }

    override fun getComponent(): JComponent =
        JPanel(BorderLayout()).apply {
            add(PropertiesPreCommitHandler.checkBox, "West")
        }
}