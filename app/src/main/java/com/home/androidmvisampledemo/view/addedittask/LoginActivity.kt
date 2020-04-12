package com.home.androidmvisampledemo.view.addedittask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.androidmvisampledemo.R
import com.home.androidmvisampledemo.intent.LoginIntentFactory
import com.home.androidmvisampledemo.model.LoginModelStore
import com.home.androidmvisampledemo.model.LoginState
import com.home.androidmvisampledemo.common.util.replaceFragmentInActivity
import com.jakewharton.rxrelay2.PublishRelay
import com.home.androidmvisampledemo.view.EventObservable
import com.home.androidmvisampledemo.view.StateSubscriber
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class LoginActivity : AppCompatActivity(),
    EventObservable<LoginViewEvent>, StateSubscriber<LoginState> {

    @Inject
    lateinit var modelStore: LoginModelStore
    @Inject
    lateinit var intentFactory: LoginIntentFactory
    private val nothingRelay = PublishRelay.create<LoginViewEvent.NothingChange>()
    private val clearRelay = PublishRelay.create<LoginViewEvent.ClearChange>()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.findFragmentById(R.id.constraint_layout_container) as LoginFragment?
            ?: LoginFragment().also {
                replaceFragmentInActivity(it, R.id.constraint_layout_container)
            }
    }

    override fun onResume() {
        super.onResume()
        disposables += modelStore.modelState().subscribeToState()
        disposables += events().subscribe(intentFactory::process)
    }

    override fun onPause() {
        disposables.clear()
        super.onPause()
    }

    override fun events(): Observable<LoginViewEvent> {
        return Observable.merge(clearRelay, nothingRelay)
    }

    override fun Observable<LoginState>.subscribeToState(): Disposable {
        return CompositeDisposable().also { innerDisposables ->
            innerDisposables += subscribe {
                when (it) {
                    LoginState.Closed -> {
                        finish()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        clearRelay.accept(LoginViewEvent.ClearChange)
        super.onBackPressed()
    }
}