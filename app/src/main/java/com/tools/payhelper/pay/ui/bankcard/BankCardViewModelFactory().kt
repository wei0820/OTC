package com.jingyu.pay.ui.bankcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BankCradViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(BankCardViewModel::class.java)){
            return BankCardViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}