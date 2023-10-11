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
import com.tools.payhelper.pay.ui.sellrecord.SellRecordData
import kotlinx.coroutines.launch

class SellRecodeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    var  orderDateModel = SellRecordDateModel()
    var  paymentMatchingData = MutableLiveData<SellRecordData>()


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

}
