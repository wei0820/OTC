package com.jingyu.pay.ui.bankcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PaymentViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)){
            return PaymentViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}