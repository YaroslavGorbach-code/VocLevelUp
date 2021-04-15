package com.example.yaroslavgorbach.voclevelup.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Event<T>(val data: T) {

    var consumed: Boolean = false
        private set

    fun consume(consumer: (T) -> Unit) {
        if (!consumed) {
            consumer(data)
            consumed = true
        }
    }
}

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

fun <T> MutableLiveEvent<T>.send(data: T) = postValue(Event(data))
fun MutableLiveEvent<Unit>.send() = postValue(Event(Unit))

fun <T> LiveEvent<T>.consume(lifecycleOwner: LifecycleOwner, consumer: (T) -> Unit) {
    observe(lifecycleOwner) { it.consume(consumer) }
}

fun LiveEvent<Unit>.consume(lifecycleOwner: LifecycleOwner, consumer: () -> Unit) {
    observe(lifecycleOwner) { it.consume { consumer() } }
}