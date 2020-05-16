package com.example.app_37_brilliantapp

import androidx.lifecycle.Observer

class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun handleOrNull(): T? {
        return if (hasBeenHandled)
            null
        else {
            hasBeenHandled = true
            content
        }
    }
}

class EventObserver<T>(private val eventHandler: (T) -> Unit): Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.handleOrNull()?.let {
            eventHandler(it)
        }
    }
}