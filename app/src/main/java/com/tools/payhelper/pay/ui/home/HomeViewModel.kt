package com.jingyu.pay.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jingyu.pay.ui.login.LoginDateModel
import com.jingyu.pay.ui.order.OrderDateModel
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.home.BuyData
import com.tools.payhelper.pay.ui.home.ExrateData
import com.tools.payhelper.pay.ui.home.PaymentIsValidData
import com.tools.payhelper.pay.ui.home.PaymentMatchingData
import com.tools.payhelper.pay.ui.home.StartBuyData
import com.tools.payhelper.pay.ui.notifications.UserinfoData
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

    var  paymentMatchingData = MutableLiveData<com.tools.payhelper.pay.ui.order.PaymentMatchingData>()

    var exrateData = MutableLiveData<ExrateData>()
    var data = MutableLiveData<UserinfoData>()
    var mPaymentIsValidData = MutableLiveData<PaymentIsValidData>()



    fun getBuySetting(context: Context,paymentlimit : String,paymentMax : String) : LiveData<StartBuyData> {
            homeViewModel.setBuySetting(context,paymentlimit,paymentMax, object : HomeDateModel.BuyResponse {
                override fun getResponse(s: String) {
                    viewModelScope.launch {
                        if (!s.isEmpty()) {
                            Log.d("Jack",s)


                            var data = Gson().fromJson(s, StartBuyData::class.java)

                            startBuy.value = data

                        }else{
                            ToastManager.showToastCenter(context,"error")
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
                Log.d("Jack＿getBuyDataList",s)
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,BuyData::class.java)
                        buyData.value = data
                    }else{
                        ToastManager.showToastCenter(context,"error")
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
                        var data = Gson().fromJson(s,PaymentMatchingData::class.java)
                        mPaymentMatchingData.value = data
                    }else{
                        ToastManager.showToastCenter(context,"error")
                    }
                }
            }

            override fun getFailure(s: String) {
            }
        })


        return  mPaymentMatchingData
    }


    fun getPaymentMatching(context: Context) : LiveData<com.tools.payhelper.pay.ui.order.PaymentMatchingData>{
        homeViewModel.getPaymentMatching(context, object : OrderDateModel.OrderResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {

                    if (!s.isEmpty()){
                        Log.d("Jack",s);
                        var data = Gson().fromJson(s,
                            com.tools.payhelper.pay.ui.order.PaymentMatchingData::class.java);
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

    fun getExrateData(context: Context) :LiveData<ExrateData>{
        homeViewModel.getExrate(context,object : HomeDateModel.BuyResponse{
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
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
    fun getUserInfo(context: Context) : LiveData<UserinfoData>{
        homeViewModel.getUserinfo(context, object : HomeDateModel.BuyResponse {
            override fun getResponse(s: String) {
                if (!s.isEmpty()){
                    viewModelScope.launch {
                        var ud = Gson().fromJson(s, UserinfoData::class.java)
                        data.value = ud


                    }
                }
            }

            override fun getFailure(s: String) {

            }

        })
        return  data
    }

    fun getPaymentIsValid(context: Context,id:String) : LiveData<PaymentIsValidData>{
        homeViewModel.getPaymentIsValid(context,id,object :HomeDateModel.BuyResponse{
            override fun getResponse(s: String) {
                Log.d("Jack",s)
                if (!s.isEmpty()){
                    viewModelScope.launch {
                        var ud = Gson().fromJson(s, PaymentIsValidData::class.java)
                        mPaymentIsValidData.value = ud


                    }
                }
            }

            override fun getFailure(s: String) {
            }

        })
        return  mPaymentIsValidData
    }

}