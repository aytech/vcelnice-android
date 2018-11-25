package cz.vcelnicerudna

import android.os.Handler
import android.os.HandlerThread

class AppDatabaseWorkerThread(threadName: String) : HandlerThread(threadName) {
    private lateinit var workerHandler: Handler

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        workerHandler = Handler(looper)
    }

    fun postTask(task: Runnable) {
        if (this::workerHandler.isInitialized) {
            workerHandler.post(task)
        } else {
            postTask(task)
        }
    }
}