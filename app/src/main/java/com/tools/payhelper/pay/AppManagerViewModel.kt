package com.tools.payhelper.pay

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tools.payhelper.pay.ui.notifications.UserinfoData
import kotlinx.coroutines.launch

class AppManagerViewModel : ViewModel() {

    var appManagerDateModel = AppManagerDateModel()
    var data = MutableLiveData<UserinfoData>()


    fun  getUserInfo(context: Context) :LiveData<UserinfoData>{
        appManagerDateModel.getUserinfo(context,object : AppManagerDateModel.AppManagerResponse{
            override fun getResponse(s: String) {
                if(!s.isEmpty()){
                    var data = MutableLiveData<UserinfoData>()
                        viewModelScope.launch {
                        var ud = Gson().fromJson(s, UserinfoData::class.java)
                        data.value = ud


                    }
                }
            }
        })
        return  data
    }
}