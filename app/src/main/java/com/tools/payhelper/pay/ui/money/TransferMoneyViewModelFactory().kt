package com.jingyu.pay.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TransferMoneyViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(TransferMoneyViewModel::class.java)){
            return TransferMoneyViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}