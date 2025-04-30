package com.jingyu.pay.ui.notifications

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jingyu.pay.ui.dashboard.SellDateModel
import com.tools.payhelper.pay.Constant
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ui.login.PostDBData
import com.tools.payhelper.pay.ui.login.SSLSocketClient
import com.tools.payhelper.pay.ui.money.BuyUsdtListData
import com.tools.payhelper.pay.ui.money.PostUsdtData
import com.tools.payhelper.pay.ui.money.TransferData
import com.tools.payhelper.pay.ui.money.TransferListData
import com.tools.payhelper.pay.ui.money.UsdtinfoData
import com.tools.payhelper.pay.ui.notifications.UserinfoData
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class TransferMoneyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
    var data = MutableLiveData<UserinfoData>()
    var mTransdata = MutableLiveData<TransferData>()
    var mTransdataList = MutableLiveData<TransferListData>()
    var mUsdtinfoData = MutableLiveData<UsdtinfoData>()

    var mPostDBData = MutableLiveData<PostUsdtData>()

    var transferMoneyDateModel = TransferMoneyDateModel()

    var mBuyUsdtListData = MutableLiveData<BuyUsdtListData>()


    fun setPostTransMoneyData(context: Context,loginId:String,score:Double,code:String): LiveData<TransferData>{
        transferMoneyDateModel.setPostTransMoneyData(context,loginId,score,code, object :
            TransferMoneyDateModel.OrderResponse {
            override fun getResponse(s: String) {
                if (!s.isEmpty()){
                    viewModelScope.launch {
                        var ud = Gson().fromJson(s, TransferData::class.java)
                        mTransdata.value = ud


                    }
                }
            }

            override fun getFailure(s: String) {


            }

        })
        return  mTransdata

    }

    fun setPostUsdtData(context: Context,score:Double,code:String,hashvaule:String): LiveData<PostUsdtData>{
        transferMoneyDateModel.setPostUsdtata(context,score,code,hashvaule, object :
            TransferMoneyDateModel.OrderResponse {
            override fun getResponse(s: String) {
                if (!s.isEmpty()){
                    viewModelScope.launch {
                        Log.d("usdt",s);

                        var ud = Gson().fromJson(s, PostUsdtData::class.java)
                        mPostDBData.value = ud


                    }
                }
            }

            override fun getFailure(s: String) {


            }

        })
        return  mPostDBData

    }

    fun getUserInfo(context: Context): LiveData<UserinfoData>{
        transferMoneyDateModel.getUserinfo(context, object : TransferMoneyDateModel.OrderResponse {
            override fun getResponse(s: String) {
                if (!s.isEmpty()){
                    Log.d("trans",s)
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

    fun getTransList(context: Context) : LiveData<TransferListData>{
        transferMoneyDateModel.getTransList(context, object : PersonalDateModel.OrderResponse {
            override fun getResponse(s: String) {
                if (!s.isEmpty()){
                    Log.d("Jack",s);

                    viewModelScope.launch {
                        var ud = Gson().fromJson(s, TransferListData::class.java)
                        mTransdataList.value = ud


                    }
                }
            }

            override fun getFailure(s: String) {
            }

        })
        return  mTransdataList
    }


    fun getUSDTInfo(context: Context) : LiveData<UsdtinfoData>{
        transferMoneyDateModel.getUSDTInfo(context, object : PersonalDateModel.OrderResponse {
            override fun getResponse(s: String) {

                if (!s.isEmpty()){

                    viewModelScope.launch {
                        var ud = Gson().fromJson(s, UsdtinfoData::class.java)
                        mUsdtinfoData.value = ud


                    }
                }
            }

            override fun getFailure(s: String) {
            }

        })
        return  mUsdtinfoData
    }
    fun getUsdtList(context: Context): LiveData<BuyUsdtListData>{
        transferMoneyDateModel.getUSDTList(context, object : PersonalDateModel.OrderResponse {
            override fun getResponse(s: String) {

                if (!s.isEmpty()){
                    Log.d("usdt",s);

                    viewModelScope.launch {
                        var ud = Gson().fromJson(s, BuyUsdtListData::class.java)
                        mBuyUsdtListData.value = ud


                    }
                }
            }

            override fun getFailure(s: String) {
            }

        })
        return  mBuyUsdtListData

    }
}