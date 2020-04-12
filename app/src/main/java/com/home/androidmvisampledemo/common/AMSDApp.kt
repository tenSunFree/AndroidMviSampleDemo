package com.home.androidmvisampledemo.common

import android.app.Application
import com.home.androidmvisampledemo.common.di.ToothpickActivityLifecycleCallbacks
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieApplicationModule

class AMSDApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDiRootScope()
    }

    private fun initDiRootScope() {
        Toothpick.inject(this, openApplicationScope(this))
        registerActivityLifecycleCallbacks(ToothpickActivityLifecycleCallbacks())
    }

    private fun openApplicationScope(app: Application): Scope {
        return Toothpick.openScope(app).apply {
            installModules(
                    SmoothieApplicationModule(app)
            )
        }
    }
}