package com.tools.payhelper.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jingyu.pay.ui.login.LoginViewModel

class LoginViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")



    }
}