package com.jingyu.pay.ui.accountchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AccountChangeViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AccountChangeViewModel::class.java)){
            return AccountChangeViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}