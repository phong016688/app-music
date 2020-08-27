package com.example.sunmusic.data.executor

import com.example.sunmusic.BuildConfig
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ThreadFactory
import kotlin.LazyThreadSafetyMode.SYNCHRONIZED

interface AppExecutor {
    fun <T> create(callable: () -> T): Future<T>
}

class AppExecutorImpl private constructor() : AppExecutor {
    private val executor = Executors.newFixedThreadPool(N_THREAD, MyThreadFactory())

    companion object {
        const val N_THREAD = 5
        val instance by lazy(SYNCHRONIZED) { AppExecutorImpl() }
    }

    override fun <T> create(callable: () -> T): Future<T> {
        return executor.submit(callable)
    }

    class MyThreadFactory : ThreadFactory {
        private var count = 0
        override fun newThread(p0: Runnable?): Thread {
            val nameThread = "${BuildConfig.APPLICATION_ID} - Thread - ${count++}"
            return Thread(p0, nameThread)
        }
    }
}
