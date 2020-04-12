package com.home.androidmvisampledemo.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginModelStore @Inject constructor() :
    ModelStore<LoginState>(LoginState.Closed)
