package com.home.androidmvisampledemo.view.addedittask

sealed class LoginViewEvent {
    data class EmailChange(val email: String) : LoginViewEvent()
    data class PasswordChange(val password: String) : LoginViewEvent()
    object ClearChange : LoginViewEvent()
    object NothingChange : LoginViewEvent()
    object SaveChange : LoginViewEvent()
}