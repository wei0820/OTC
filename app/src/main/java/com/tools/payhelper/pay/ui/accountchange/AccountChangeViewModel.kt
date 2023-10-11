package com.jingyu.pay.ui.accountchange

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tools.payhelper.pay.ui.accountchange.AccountChange
import kotlinx.coroutines.launch

class AccountChangeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    var accountChangeDateModel = AccountChangeDateModel()

    var  accountChangeData = MutableLiveData<AccountChange>()


    fun  getAccountChangeData(context: Context) : LiveData<AccountChange>{

        accountChangeDateModel.getAccountChangeDate(context, object : AccountChangeDateModel.AccountCahngeResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){

                        var data = Gson().fromJson(s, AccountChange::class.java)

                        if (data!=null){
                            accountChangeData.value = data

                        }
                    }

                }
            }

        })
        return  accountChangeData




    }

}