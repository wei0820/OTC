package com.jingyu.pay.ui.notifications

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jingyu.pay.ui.dashboard.SellDateModel
import com.tools.payhelper.pay.ui.notifications.UserinfoData
import kotlinx.coroutines.launch

class TransferMoneyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
    var data = MutableLiveData<UserinfoData>()
    var transferMoneyDateModel = TransferMoneyDateModel()



    fun getUserInfo(context: Context): LiveData<UserinfoData>{
        transferMoneyDateModel.getUserinfo(context, object : TransferMoneyDateModel.OrderResponse {
            override fun getResponse(s: String) {
                if (!s.isEmpty()){
                    viewModelScope.launch {
                        viewModelScope.launch {
                            var ud = Gson().fromJson(s, UserinfoData::class.java)
                            data.value = ud


                        }
                    }
                }
            }

            override fun getFailure(s: String) {
            }

        })
        return  data

    }

}