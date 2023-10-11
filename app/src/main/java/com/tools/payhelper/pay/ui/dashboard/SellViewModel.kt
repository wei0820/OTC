package com.jingyu.pay.ui.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tools.payhelper.pay.ui.dashboard.CollectionQueueData
import com.tools.payhelper.pay.ui.dashboard.CollectionQueueOffData
import com.tools.payhelper.pay.ui.dashboard.ConfirmData
import com.tools.payhelper.pay.ui.dashboard.SellListData
import kotlinx.coroutines.launch

class SellViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }


    var sellDateModel = SellDateModel()
    var setSellSettingData = MutableLiveData<CollectionQueueData>()
    var setCloseSellSettingData = MutableLiveData<CollectionQueueOffData>()

    var mSellListData = MutableLiveData<SellListData>()
    var confirmData  = MutableLiveData<ConfirmData>()


    fun setSellSetting(context: Context) : LiveData<CollectionQueueData>{
        sellDateModel.setSellSetting(context, object : SellDateModel.SellResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,CollectionQueueData::class.java)
                        if (data!=null){
                            setSellSettingData.value = data
                        }
                    }
                }
            }

        })

        return  setSellSettingData
    }

    fun  setCloseSellSetting(context: Context) : LiveData<CollectionQueueOffData>{
        sellDateModel.setCloseSellSetting(context, object : SellDateModel.SellResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,CollectionQueueOffData::class.java)
                        if (data!=null){
                            setCloseSellSettingData.value = data
                        }
                    }
                }            }

        })



        return  setCloseSellSettingData
    }

    fun getSellList(context: Context)  : LiveData<SellListData>{

        sellDateModel.getSellList(context, object : SellDateModel.SellResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,SellListData::class.java)
                        if (data!=null){
                            mSellListData.value = data
                        }

                    }

                }

            }

        })

        return mSellListData

    }

    fun getComfirmOrder(id : String , userName : String,context: Context) :LiveData<ConfirmData>{
        sellDateModel.setConfirmOrder(id,userName,context, object :SellDateModel.SellResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {

                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,ConfirmData::class.java)
                        if (data!=null){
                        }
                    }
                }
            }

        })


        return  confirmData

    }

}