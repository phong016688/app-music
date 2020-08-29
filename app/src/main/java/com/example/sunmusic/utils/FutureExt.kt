package com.example.sunmusic.utils

import com.example.sunmusic.data.executor.AppExecutorImpl
import java.lang.Exception
import java.util.concurrent.Future

fun <T> Future<T>.subscribe(
    onSuccess: (T) -> Unit,
    onError: (Throwable) -> Unit
) {
    val errorCallback: (Exception) -> Unit = {
        cancel(!isCancelled || isDone)
        onError(Throwable(it.message))
    }
    AppExecutorImpl.instance.runAsyncOnMain(::get, onSuccess, errorCallback)
}
