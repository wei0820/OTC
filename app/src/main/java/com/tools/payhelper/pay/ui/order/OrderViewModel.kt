package com.jingyu.pay.ui.order

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
import com.tools.payhelper.pay.ui.order.PaymentMatchingData
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    var  orderDateModel = OrderDateModel()
    var  paymentMatchingData = MutableLiveData<PaymentMatchingData>()


    fun getPaymentMatching(context: Context) : LiveData<PaymentMatchingData>{
        orderDateModel.getPaymentMatching(context, object : OrderDateModel.OrderResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,PaymentMatchingData::class.java);
                        if (data!=null){
                            paymentMatchingData.value = data
                        }
                    }
                }
            }

            override fun getFailure(s: String) {
            }

        })
        return paymentMatchingData;
    }

}
