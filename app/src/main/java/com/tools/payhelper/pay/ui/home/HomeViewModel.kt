package com.jingyu.pay.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tools.payhelper.pay.ui.home.BuyData
import com.tools.payhelper.pay.ui.home.PaymentMatchingData
import com.tools.payhelper.pay.ui.home.StartBuyData
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    var homeViewModel = HomeDateModel()
    var  startBuy = MutableLiveData<StartBuyData>()

    var  buyData = MutableLiveData<BuyData>()
    var  mPaymentMatchingData = MutableLiveData<PaymentMatchingData>()




    fun getBuySetting(context: Context,paymentlimit : String,paymentMax : String) : LiveData<StartBuyData> {
            homeViewModel.setBuySetting(context,paymentlimit,paymentMax, object : HomeDateModel.BuyResponse {
                override fun getResponse(s: String) {
                    viewModelScope.launch {
                        if (!s.isEmpty()) {
                            var data = Gson().fromJson(s, StartBuyData::class.java)

                            startBuy.value = data

                        }
                    }
                }

                override fun getFailure(s: String) {
                }

            })
            return  startBuy


        }


    fun getBuyDataList(context: Context) : LiveData<BuyData>{
        homeViewModel.getBuyOrederList(context,object : HomeDateModel.BuyResponse{
            override fun getResponse(s: String) {
                Log.d("Jack",s)
                viewModelScope.launch {
                    if (!s.isEmpty()){

                        var data = Gson().fromJson(s,BuyData::class.java)
                        buyData.value = data
                    }
                }
            }

            override fun getFailure(s: String) {
            }

        })
        return  buyData
    }

    fun getPaymentMatchingData(context: Context,id : String) :LiveData<PaymentMatchingData>{
        homeViewModel.getPayment(context,id,object : HomeDateModel.BuyResponse{
            override fun getResponse(s: String) {

                viewModelScope.launch {
                    if (!s.isEmpty()){
                        Log.d("Jack",s);
                        var data = Gson().fromJson(s,PaymentMatchingData::class.java)
                        mPaymentMatchingData.value = data
                    }
                }            }

            override fun getFailure(s: String) {
            }
        })


        return  mPaymentMatchingData
    }

}