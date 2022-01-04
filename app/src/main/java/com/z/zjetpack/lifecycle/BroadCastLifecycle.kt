package com.z.zjetpack.lifecycle

import android.content.IntentFilter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.z.zjetpack.MyBroadcastReceiver

class BroadCastLifecycle(private val activity: AppCompatActivity) : LifecycleObserver {

    private val myBroadcastReceiver by lazy { MyBroadcastReceiver() }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun lifeOnResume() {
        Log.v("zx", "注册广播")
        val intentFilter = IntentFilter("zxmsg")
        activity.registerReceiver(myBroadcastReceiver, intentFilter)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun lifeOnPause() {
        Log.v("zx", "取消注册广播")
        activity.unregisterReceiver(myBroadcastReceiver)
    }
}