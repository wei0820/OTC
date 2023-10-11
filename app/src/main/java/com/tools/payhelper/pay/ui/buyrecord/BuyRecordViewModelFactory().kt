package com.jingyu.pay.ui.buyrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BuyRecordViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(BuyRecordViewModel::class.java)){
            return BuyRecordViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}