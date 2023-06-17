package com.example.tradeu.helper

class SingleEvent<out T>(private val content: T) {
    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
    private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}