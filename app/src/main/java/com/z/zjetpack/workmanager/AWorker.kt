package com.z.zjetpack.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class AWorker(context: Context, workerParams: WorkerParameters) :Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.v("zx","回调到AWorker")
        return Result.success()
    }
}