package com.jingyu.pay.ui.buyrecord

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tools.payhelper.pay.ui.buyrecord.BuyRecordData
import kotlinx.coroutines.launch

class BuyRecordViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    var accountChangeDateModel = BuyRecodeDateModel()

    var  buyRecordData = MutableLiveData<BuyRecordData>()

    fun  getBuyRecordList(context: Context,date:String):LiveData<BuyRecordData>{

        accountChangeDateModel.getBuyRecordList(context,date, object : BuyRecodeDateModel.AccountCahngeResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,BuyRecordData::class.java)
                        if (data != null){
                            buyRecordData.value = data
                        }

                    }
                }

            }

        })


        return buyRecordData
    }


}