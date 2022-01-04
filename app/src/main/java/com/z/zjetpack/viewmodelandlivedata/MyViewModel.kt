package com.z.zjetpack.viewmodelandlivedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel:ViewModel() {

    val count = MutableLiveData<Int>()


    var numbers = 0

    fun add(){
        //ui线程：value
        //非ui现场：postvalue
        CoroutineScope(Dispatchers.IO).launch {
            count.postValue(999999)
            withContext(Dispatchers.IO) {
                count.postValue(88888)
                withContext(Dispatchers.Main) {
                    count.value = 77777
                }
                count.postValue(66666)
            }
        }
    }
}