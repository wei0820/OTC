package com.jingyu.pay.ui.order

import android.content.Context
import android.util.Log
import com.tools.payhelper.pay.PayHelperUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class OrderDateModel {


    var BaseUrl : String = "https://api2.channel-sign.com/"

    fun getPaymentMatching(context: Context, orderResponse: OrderResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(BaseUrl + "api/user/GetPaymentMatching")
            .get()
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

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