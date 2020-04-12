package com.home.androidmvisampledemo.intent

import com.home.androidmvisampledemo.model.HomeState
import com.home.androidmvisampledemo.model.LoginModelStore
import com.home.androidmvisampledemo.model.LoginState
import com.home.androidmvisampledemo.model.LoginState.Closed
import com.home.androidmvisampledemo.model.LoginState.Editing
import com.home.androidmvisampledemo.model.TasksModelStore
import com.home.androidmvisampledemo.view.addedittask.LoginViewEvent
import com.home.androidmvisampledemo.view.addedittask.LoginViewEvent.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginIntentFactory @Inject constructor(
    private val loginModelStore: LoginModelStore,
    private val tasksModelStore: TasksModelStore
) {

    fun process(viewEvent: LoginViewEvent) {
        loginModelStore.process(toIntent(viewEvent))
    }

    private fun toIntent(viewEvent: LoginViewEvent): Intent<LoginState> {
        return when (viewEvent) {
            is EmailChange -> buildEmailChangeIntent(viewEvent)
            is PasswordChange -> buildPasswordChangeIntent(viewEvent)
            is ClearChange -> buildClearIntent()
            is NothingChange -> buildNothingIntent()
            is SaveChange -> buildSaveIntent()
        }
    }

    private fun buildSaveIntent() = editorIntent<Editing> {
        save().run {
            val intent = HomeIntentFactory.buildEmailPasswordTaskIntent(
                email, password
            )
            tasksModelStore.process(intent)
            email()
        }
    }

    private fun buildNothingIntent() = editorIntent<Editing> {
        nothing().run { nothing() }
    }

    private fun buildClearIntent() = editorIntent<Editing> {
        clear().run {
            val intent = HomeIntentFactory.buildEmailPasswordTaskIntent(email, password)
            tasksModelStore.process(intent)
            clear()
        }
    }

    companion object {
        inline fun <reified S : LoginState> editorIntent(
            crossinline block: S.() -> LoginState
        ): Intent<LoginState> {
            return intent {
                (this as? S)?.block()
                    ?: throw IllegalStateException("editorIntent encountered an inconsistent State. [Looking for ${S::class.java} but was ${this.javaClass}]")
            }
        }

        fun buildAddTaskIntent(task: HomeState) = editorIntent<Closed> {
            startLogin(task)
        }

        private fun buildEmailChangeIntent(viewEvent: EmailChange) = editorIntent<Editing> {
            edit { copy(email = viewEvent.email) }
        }

        private fun buildPasswordChangeIntent(viewEvent: PasswordChange) = editorIntent<Editing> {
            edit { copy(password = viewEvent.password) }
        }
    }
}