package com.home.androidmvisampledemo.intent

import com.home.androidmvisampledemo.model.HomeState
import com.home.androidmvisampledemo.model.LoginModelStore
import com.home.androidmvisampledemo.model.TasksModelStore
import com.home.androidmvisampledemo.view.tasks.HomeViewEvent
import com.home.androidmvisampledemo.view.tasks.HomeViewEvent.NewTaskClick
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeIntentFactory @Inject constructor(
    private val tasksModelStore: TasksModelStore,
    private val loginModelStore: LoginModelStore
) {
    fun process(event: HomeViewEvent) {
        tasksModelStore.process(toIntent(event))
    }

    private fun toIntent(viewEvent: HomeViewEvent): Intent<HomeState> {
        return when (viewEvent) {
            NewTaskClick -> buildNewTaskIntent()
        }
    }

    private fun buildNewTaskIntent(): Intent<HomeState> = sideEffect {
        val addIntent = LoginIntentFactory.buildAddTaskIntent(HomeState())
        loginModelStore.process(addIntent)
    }

    companion object {
        fun buildEmailPasswordTaskIntent(email: String, password: String)
                : Intent<HomeState> = intent {
            copy(email = email, password = password)
        }
    }
}