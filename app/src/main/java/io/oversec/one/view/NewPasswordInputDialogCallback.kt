package io.oversec.one.view

interface NewPasswordInputDialogCallback {
    fun positiveAction(pw: CharArray)
    fun neutralAction()
}
