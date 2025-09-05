package io.oversec.one.ui

interface NewPasswordInputDialogCallback {
    fun positiveAction(pw: CharArray)
    fun neutralAction()
}
