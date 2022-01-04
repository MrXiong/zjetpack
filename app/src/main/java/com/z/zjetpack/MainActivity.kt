package com.z.zjetpack

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.z.zjetpack.databinding.ActivityMainBinding
import com.z.zjetpack.lifecycle.BroadCastLifecycle
import com.z.zjetpack.viewmodelandlivedata.MyViewModel
import com.z.zjetpack.viewmodelandlivedata.SeekViewModel
import com.z.zjetpack.workmanager.MyWorker
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation

class MainActivity : AppCompatActivity() {

    //bsc usdt 和 YCC 代理提币，等待查询当天可提额度接口

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val myViewModel by viewModels<MyViewModel>()

    private val seekViewModel by viewModels<SeekViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListener()
        initLifecycle()
        initViewModelAndLiveData()
        initWorkManager()
    }


    private fun initListener() {
        binding.btnSend.setOnClickListener {
            val intent = Intent("zxmsg")
            sendBroadcast(intent)
        }
        binding.btnAdd.setOnClickListener {
            //viewmodel的数据不跟生命周期走，不管生命周期到了哪一步，viewmodel的数据都存在
            //哪怕acitivty销毁了，数据仍然存在
            //不要往viewmodel中传入context，会导致内存泄露
            //不要使用context，请使用AndroidViewModel中的Application
            //比如屏幕旋转，viewmodel中的number是不会变化的
            binding.btnAdd.text = (++myViewModel.numbers).toString()

        }

        binding.btnRandom.setOnClickListener {
            myViewModel.add()
        }

        binding.btnDowork.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                doWorking()
            }
        }
        binding.btnWorkchain.setOnClickListener {
            //doWorkChain()
            doWorkChains()
        }


        seekViewModel.cprogress.observe(this, Observer {
            Log.v("zx", "mmmmm返回$it")
        })

    }

    private fun initLifecycle() {
        lifecycle.addObserver(BroadCastLifecycle(this))
    }

    private fun initViewModelAndLiveData() {
        myViewModel.count.observe(this, Observer {
            binding.btnRandom.text = it.toString()
        })
    }

    private fun initWorkManager() {
        //WorkManager在真机上面不一定会有效，因为小米华为可能对系统进行了修改，所以要对不同厂商的手机做适配

        //workmanager的使用方法
        //将任务提交给系统
        //观察任务的状态
        //取消任务
        //参数传递
        //周期性任务
        //任务链
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doWorking() {
        //设置触发条件
        val constraints = Constraints.Builder()

            //需要有网络才可以触发
            //NetworkType.NOT_REQUIRED 对网络没有要求
            //NetworkType.CONNECTED 网络连接时执行
            //NetworkType.UNMETERED 不计费的网络，比如wifi下执行
            //NetworkType.NOT_ROAMING 非漫游网络下执行
            //NetworkType.METERED 计费网络下比如3g，4g下执行
            //注意：不代表恢复网络后立马执行，由系统决定的
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            //不在电量不足的时候执行
            //.setRequiresBatteryNotLow(true)
            //在充电时执行
            //.setRequiresCharging(true)
            //不在存储容量不足时执行
            //.setRequiresStorageNotLow(true)
            //在待机状态下执行
            //最低api为23
            //.setRequiresDeviceIdle(true)
            .build()

        val data = Data.Builder()
            .putString("name", "诸葛亮")
            .build()

        //配置任务
        //配置只执行一次的任务
        //val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWork::class.java).build()
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            //设置触发条件
            .setConstraints(constraints)
            //设置延迟执行(延迟5秒执行)
            .setInitialDelay(5, TimeUnit.SECONDS)
            //指数退避策略(最低api为26)
            //.setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofSeconds(2))
            //设置tag标签
            .addTag("onetag")
            //参数传递到work中
            .setInputData(data)
            .build()

        //周期任务的时间不能少于15分钟
        val periodicWorkRequest = PeriodicWorkRequestBuilder<MyWorker>(Duration.ofMinutes(15))
            .build()


        //任务提交给workmanager
        val workManager = WorkManager.getInstance(this)


        //观察任务状态(ENQUEUED：已加入队列，SUCCEEDED：执行成功)
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this, Observer {
            Log.v("zx", "onetime是：$it.toString()")
            if (it != null && it.state == WorkInfo.State.SUCCEEDED) {
                val wResult = it.outputData.getString("wresult")
                Log.v("zx", "观察执行结果：$wResult")
            }
        })


        workManager.enqueue(oneTimeWorkRequest)

        //取消任务（5秒后执行，我们在2秒后就取消，测试是否还会执行）
        /*    Timer().schedule(object : TimerTask() {
                override fun run() {
                    Log.v("zx", "2秒后取消")
                    workManager.cancelWorkById(oneTimeWorkRequest.id)
                }

            }, 2000)*/
    }

    //简单任务链
    private fun doWorkChain() {
        val aOneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            //设置tag标签
            .addTag("atag")
            .build()
        val bOneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            //设置tag标签
            .addTag("btag")
            .build()


        //任务提交给workmanager
        val workManager = WorkManager.getInstance(this)
        workManager
            .beginWith(aOneTimeWorkRequest)
            .then(bOneTimeWorkRequest)
            .enqueue()
    }

    //复杂任务链，任务链组合
    private fun doWorkChains() {
        val aOneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            //设置tag标签
            .addTag("atag")
            .build()
        val bOneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            //设置tag标签
            .addTag("btag")
            .build()
        val cOneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            //设置tag标签
            .addTag("ctag")
            .build()
        val dOneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            //设置tag标签
            .addTag("dtag")
            .build()
        val eOneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            //设置tag标签
            .addTag("etag")
            .build()


        //任务提交给workmanager
        val workManager = WorkManager.getInstance(this)
        val abWorkContinuation = workManager
            .beginWith(aOneTimeWorkRequest)
            .then(bOneTimeWorkRequest)

        val cdWorkContinuation = workManager
            .beginWith(cOneTimeWorkRequest)
            .then(dOneTimeWorkRequest)

        val cons = arrayListOf(abWorkContinuation, cdWorkContinuation)
        WorkContinuation.combine(cons)
            .then(eOneTimeWorkRequest)
            .enqueue()


    }


}