package com.jingyu.pay.ui.notifications

import android.content.Context
import android.util.Log
import com.tools.payhelper.pay.Constant
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ui.login.SSLSocketClient

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.Console
import java.io.IOException
import java.util.concurrent.TimeUnit

class TransferMoneyDateModel {


    var BaseUrl : String = Constant.API_URL
    val callMap = HashMap<String, Call>()

    fun setPostTransMoneyData(context: Context,token:String,orderResponse: OrderResponse){
        var jsonObject= JSONObject()
        jsonObject.put("token",token)
        jsonObject.put("token",token)
        jsonObject.put("token",token)
        jsonObject.put("token",token)

        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
//        val client = OkHttpClient()
        val client = OkHttpClient.Builder()
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(BaseUrl + "api/android/MerchantOrders/GetMerchantPublicOrders")
            .post(requestBody)
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .build()

        if (callMap.containsKey(loginid)) {
            val call = callMap[loginid]
            if (call != null && !call.isCanceled()) {
                call.cancel()
            }
        }


        val call = client.newCall(request)
        callMap.put(loginid,call)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                orderResponse.getFailure(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                orderResponse.getResponse( response.body?.string()!!)
            }
        })

    }


    interface OrderResponse{
        fun getResponse(s : String)
        fun getFailure(s: String)
    }
}