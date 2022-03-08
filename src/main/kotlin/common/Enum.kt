package common

import com.intellij.psi.PsiEnumConstant
import com.intellij.psi.PsiLiteralExpression

fun PsiEnumConstant.equal(enum: PsiEnumConstant): Boolean =
    this.params == enum.params

val PsiEnumConstant.params
    get() = argumentList?.expressions
        ?.map { it.children }
        ?.flatMap { it.map { (it.context as PsiLiteralExpression).value } }
