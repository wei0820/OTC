package com.jingyu.pay.ui.sellrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SellRecordViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SellRecodeViewModel::class.java)){
            return SellRecodeViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}