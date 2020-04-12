package com.home.androidmvisampledemo.view.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.home.androidmvisampledemo.R
import com.home.androidmvisampledemo.intent.HomeIntentFactory
import com.home.androidmvisampledemo.model.HomeState
import com.home.androidmvisampledemo.model.TasksModelStore
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.text
import com.home.androidmvisampledemo.view.EventObservable
import com.home.androidmvisampledemo.view.StateSubscriber
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : Fragment(),
    EventObservable<HomeViewEvent>, StateSubscriber<HomeState> {

    @Inject
    lateinit var tasksModelStore: TasksModelStore
    @Inject
    lateinit var tasksIntentFactory: HomeIntentFactory
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        disposables += events().subscribe(tasksIntentFactory::process)
        disposables += tasksModelStore.modelState().subscribeToState()
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    override fun events(): Observable<HomeViewEvent> {
        return view_login_center.clicks().map { HomeViewEvent.NewTaskClick }
    }

    override fun Observable<HomeState>.subscribeToState(): Disposable {
        return CompositeDisposable(
            map { tasksState ->
                if (tasksState.email.isEmpty() || tasksState.password.isEmpty()) {
                    image_view_background.setImageResource(R.drawable.icon_home)
                } else {
                    image_view_background.setImageResource(R.drawable.icon_home_logged)
                    view_login_center.visibility = View.GONE
                    text_view_email.text = tasksState.email
                }
            }.subscribe { text_view_email.text() }
        )
    }
}