package com.home.androidmvisampledemo.view.tasks

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.home.androidmvisampledemo.R
import com.home.androidmvisampledemo.intent.HomeIntentFactory
import com.home.androidmvisampledemo.model.LoginModelStore
import com.home.androidmvisampledemo.model.LoginState
import com.home.androidmvisampledemo.common.util.replaceFragmentInActivity
import com.home.androidmvisampledemo.view.addedittask.LoginActivity
import com.home.androidmvisampledemo.view.StateSubscriber
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class HomeActivity : AppCompatActivity(),
    StateSubscriber<LoginState> {

    @Inject
    lateinit var editorModelStore: LoginModelStore
    @Inject
    lateinit var tasksIntentFactory: HomeIntentFactory
    private val disposables = CompositeDisposable()

    override fun Observable<LoginState>.subscribeToState(): Disposable {
        return ofType<LoginState.Editing>().subscribe {
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportFragmentManager.findFragmentById(R.id.constraint_layout_container) as HomeFragment?
            ?: HomeFragment().also {
                replaceFragmentInActivity(it, R.id.constraint_layout_container)
            }
    }

    override fun onResume() {
        super.onResume()
        disposables += editorModelStore.modelState().subscribeToState()
    }

    override fun onPause() {
        disposables.clear()
        super.onPause()
    }
}