package com.z.zjetpack.viewmodelandlivedata

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.z.zjetpack.IApplication

class ContextViewModel(application: Application) : AndroidViewModel(application) {
    private fun addView(){
        getCon(getApplication<IApplication>())
    }

    private fun getCon(context: Context){

    }
}