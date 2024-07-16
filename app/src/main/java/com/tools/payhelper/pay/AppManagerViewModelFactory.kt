package com.tools.payhelper.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tools.payhelper.pay.AppManagerViewModel

class AppManagerViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AppManagerViewModel::class.java)){
            return AppManagerViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")



    }
}