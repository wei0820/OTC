package com.tools.payhelper.pay.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewsListViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(NewsListViewModel::class.java)){
            return NewsListViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}