package com.home.androidmvisampledemo.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksModelStore @Inject constructor() :
    ModelStore<HomeState>(HomeState("", ""))