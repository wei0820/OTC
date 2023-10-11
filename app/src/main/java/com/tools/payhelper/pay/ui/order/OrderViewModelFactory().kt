package com.jingyu.pay.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OrderViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(OrderViewModel::class.java)){
            return OrderViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}