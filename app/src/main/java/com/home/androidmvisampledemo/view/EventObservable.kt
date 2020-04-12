package com.home.androidmvisampledemo.view

import io.reactivex.Observable

interface EventObservable<E> {
    fun events(): Observable<E>
}