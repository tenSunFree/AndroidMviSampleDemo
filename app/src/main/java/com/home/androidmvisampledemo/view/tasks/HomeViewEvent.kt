package com.home.androidmvisampledemo.view.tasks

sealed class HomeViewEvent {
    object NewTaskClick : HomeViewEvent()
}