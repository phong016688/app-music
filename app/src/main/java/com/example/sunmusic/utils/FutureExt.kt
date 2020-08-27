package com.example.sunmusic.utils

import java.util.concurrent.Future

inline fun <T> Future<T>.subscribe(
    onSuccess: (T) -> Unit,
    onError: (Throwable) -> Unit
) {
    try {
        onSuccess(this.get())
    } catch (e: Exception) {
        cancel(!isCancelled || !isDone)
        onError(Throwable(e.message))
    }
}
