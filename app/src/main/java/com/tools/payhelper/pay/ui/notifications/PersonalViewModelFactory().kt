package com.jingyu.pay.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PersonalViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PersonalViewModel::class.java)){
            return PersonalViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}