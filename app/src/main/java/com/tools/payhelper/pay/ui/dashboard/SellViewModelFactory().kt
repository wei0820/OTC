package com.jingyu.pay.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SellViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SellViewModel::class.java)){
            return SellViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}