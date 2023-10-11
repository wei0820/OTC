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
import kotlinx.coroutines.launch

class BankCardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    var bankCardDateModel = BankCardDateModel()
    var bankCardData = MutableLiveData<BanCardListData>()
    var deleteBankCardData = MutableLiveData<DeleteBankCardData>()
    var stopBankCardData = MutableLiveData<StopBankCardData>()



    fun getBankList(context: Context):LiveData<BanCardListData>{
        bankCardDateModel.getBankCradList(context, object :BankCardDateModel.BankCardResponse {
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

    fun  deleteBankCard(context: Context,id: String) : LiveData<DeleteBankCardData>{
        bankCardDateModel.setDeleteBankCard(context,id, object :BankCardDateModel.BankCardResponse {
            override fun getResponse(s: String) {
                viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,DeleteBankCardData::class.java)
                        if (data !=null){
                            deleteBankCardData.value = data
                        }
                    }
                }
            }

        })

        return  deleteBankCardData
    }

    fun  setStopBankCard(context: Context, id : String):LiveData<StopBankCardData>{
        bankCardDateModel.setStopBankCard(context,id, object :BankCardDateModel.BankCardResponse {
            override fun getResponse(s: String) {
               viewModelScope.launch {
                    if (!s.isEmpty()){
                        var data = Gson().fromJson(s,StopBankCardData::class.java)
                        if (data !=null){
                            stopBankCardData.value = data
                        }
                    }
                }

            }

        })


        return stopBankCardData
    }

}