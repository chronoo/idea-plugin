package common

import com.intellij.psi.JavaDocTokenType
import com.intellij.psi.PsiElement
import com.intellij.psi.javadoc.PsiDocToken

val PsiElement.isCommentData
    get() = this is PsiDocToken && tokenType == JavaDocTokenType.DOC_COMMENT_DATA

val PsiElement.inTestFile: Boolean
    get() = containingFile.name.matches("^T\\d+.java$".toRegex())