package com.z.zjetpack.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val name = inputData.getString("name")
        Log.v("zx", "doWork回调了。。$name")
        //任务执行完后返回结果
        val data = Data.Builder()
            .putString("wresult", "我得到$name 啦")
            .build()
        return Result.success(data)
    }
}