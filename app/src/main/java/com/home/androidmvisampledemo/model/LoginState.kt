package com.home.androidmvisampledemo.model

sealed class LoginState {

    object Closed : LoginState() {
        fun startLogin(state: HomeState) = Editing(state)
    }

    data class Editing(val state:  HomeState) : LoginState() {
        fun edit(block: HomeState.() -> HomeState) = copy(state = state.block())
        fun save() = Save(email = state.email, password = state.password)
        fun clear() = Clear(email = "", password = "")
        fun nothing() = Closed
    }

    data class Save(val email: String, val password: String) : LoginState() {
        fun email() = Closed
    }

    data class Clear(val email: String, val password: String) : LoginState() {
        fun clear() = Closed
    }
}
