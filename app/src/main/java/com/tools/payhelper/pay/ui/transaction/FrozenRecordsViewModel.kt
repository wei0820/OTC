package com.jingyu.pay.ui.transaction

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
import com.tools.payhelper.pay.ui.transaction.FrozenRecordsData
import kotlinx.coroutines.launch

class FrozenRecordsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    var  orderDateModel = FrozenRecordsDateModel()
    var  paymentMatchingData = MutableLiveData<FrozenRecordsData>()


    fun getSellRecodeList(context: Context,date: String) : LiveData<FrozenRecordsData>{
        orderDateModel.getFrozenRecordsData(context,date, object : FrozenRecordsDateModel.OrderResponse {
            override fun getResponse(s: String) {
                if (!s.isEmpty()){
                    viewModelScope.launch {
                        var data = Gson().fromJson(s,FrozenRecordsData::class.java)
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
