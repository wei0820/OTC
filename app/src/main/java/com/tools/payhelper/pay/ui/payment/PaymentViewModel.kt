package com.jingyu.pay.ui.bankcard

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tools.payhelper.pay.ui.bankcard.BanCardListData
import com.tools.payhelper.pay.ui.bankcard.DeleteBankCardData
import com.tools.payhelper.pay.ui.bankcard.StopBankCardData
import com.tools.payhelper.pay.ui.payment.PaymentData
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    var paymentDateModel = PaymentDateModel()
    var bankCardData = MutableLiveData<BanCardListData>()
    var deleteBankCardData = MutableLiveData<DeleteBankCardData>()
    var stopBankCardData = MutableLiveData<StopBankCardData>()
    var paymentData = MutableLiveData<PaymentData>()



    fun getBankList(context: Context):LiveData<BanCardListData>{
        paymentDateModel.getBankCradList(context, object :PaymentDateModel.BankCardResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,BanCardListData::class.java)
                        bankCardData.value = data
                    }
                }
            }

        })

        return  bankCardData
    }

    fun getPaymentList(context: Context):LiveData<PaymentData>{
        paymentDateModel.getPaymentList(context, object :PaymentDateModel.BankCardResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,PaymentData::class.java)
                        paymentData.value = data
                    }
                }
            }

        })

        return  paymentData
    }




}