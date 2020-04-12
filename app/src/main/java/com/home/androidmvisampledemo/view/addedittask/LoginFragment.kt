package com.home.androidmvisampledemo.view.addedittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.home.androidmvisampledemo.R
import com.home.androidmvisampledemo.intent.LoginIntentFactory
import com.home.androidmvisampledemo.model.LoginModelStore
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.home.androidmvisampledemo.view.EventObservable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment(),
    EventObservable<LoginViewEvent> {

    @Inject
    lateinit var modelStore: LoginModelStore
    @Inject
    lateinit var intentFactory: LoginIntentFactory
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onResume() {
        super.onResume()
        disposables += events().subscribe(intentFactory::process)
    }

    override fun onPause() {
        disposables.clear()
        super.onPause()
    }

    override fun events(): Observable<LoginViewEvent> {
        return Observable.merge(
            edit_text_email.textChanges().map {
                LoginViewEvent.EmailChange(it.toString())
            },
            edit_text_password.textChanges().map {
                LoginViewEvent.PasswordChange(it.toString())
            },
            view_bottom.clicks().map {
                LoginViewEvent.SaveChange
            }
        )
    }
}