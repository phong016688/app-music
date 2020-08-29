package com.example.sunmusic.utils

interface BasePresenter<T> {
    fun onStart()
    fun onStop()
    fun setView(view: T?)
}
