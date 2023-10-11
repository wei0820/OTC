package com.jingyu.pay.ui.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GroupViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(GroupViewModel::class.java)){
            return GroupViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}