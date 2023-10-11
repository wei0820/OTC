package com.jingyu.pay.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PasswordViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PassWordViewModel::class.java)){
            return PassWordViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}