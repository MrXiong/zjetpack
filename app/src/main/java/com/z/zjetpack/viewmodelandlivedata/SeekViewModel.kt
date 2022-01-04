package com.z.zjetpack.viewmodelandlivedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SeekViewModel:ViewModel() {

     val cprogress = MutableLiveData<Int>()

    fun setProgress(progress: Int){
        cprogress.value = progress
    }


}