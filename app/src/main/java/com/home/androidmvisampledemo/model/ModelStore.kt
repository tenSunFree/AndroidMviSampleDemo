package com.home.androidmvisampledemo.model

import com.home.androidmvisampledemo.intent.Intent
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

open class ModelStore<S>(startingState: S) : Model<S> {

    private val intents = PublishRelay.create<Intent<S>>()

    private val store = intents
        .observeOn(AndroidSchedulers.mainThread())
        .scan(startingState) { oldState, intent ->
            intent.reduce(oldState)
        }
        .replay(1)
        .apply { connect() }

    override fun process(intent: Intent<S>) = intents.accept(intent)

    override fun modelState(): Observable<S> = store
}