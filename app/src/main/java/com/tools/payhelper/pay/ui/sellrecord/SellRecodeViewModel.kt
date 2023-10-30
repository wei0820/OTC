package com.jingyu.pay.ui.sellrecord

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.jingyu.pay.ui.dashboard.DashboardFragment
import com.jingyu.pay.ui.home.HomeDateModel
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.home.ExrateData
import com.tools.payhelper.pay.ui.sellrecord.SellRecordData
import kotlinx.coroutines.launch

class SellRecodeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    var  orderDateModel = SellRecordDateModel()
    var  paymentMatchingData = MutableLiveData<SellRecordData>()

    var exrateData = MutableLiveData<ExrateData>()

    fun getSellRecodeList(context: Context,date: String) : LiveData<SellRecordData>{
        orderDateModel.getSellRecordData(context,date, object : SellRecordDateModel.OrderResponse {
            override fun getResponse(s: String) {
                if (!s.isEmpty()){
                    viewModelScope.launch {
                        var data = Gson().fromJson(s,SellRecordData::class.java)
                        if (data != null){

                            paymentMatchingData.value = data
                        }
                    }

                }
            }

            override fun getFailure(s: String) {
            }

        })
        return paymentMatchingData
    }


    fun getExrateData(context: Context) :LiveData<ExrateData>{
        orderDateModel.getExrate(context,object : SellRecordDateModel.OrderResponse{
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        Log.d("Jack",s)

                        var data = Gson().fromJson(s,ExrateData::class.java)
                        exrateData.value = data
                    }else{
                        ToastManager.showToastCenter(context,"error")
                    }
                }
            }

            override fun getFailure(s: String) {
            }
        })



        return  exrateData
    }

}
