package com.jingyu.pay.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FrozenRecordsViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(FrozenRecordsViewModel::class.java)){
            return FrozenRecordsViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}