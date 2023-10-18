package com.jingyu.pay.ui.notifications

import android.content.Context
import android.util.Log
import com.tools.payhelper.pay.Constant
import com.tools.payhelper.pay.PayHelperUtils

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.Console
import java.io.IOException

class PersonalDateModel {


    var BaseUrl : String = Constant.API_URL
    fun getMerchantPublicOrders(token:String,orderResponse: OrderResponse){
        var jsonObject= JSONObject()
        jsonObject.put("token",token)
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(BaseUrl + "api/android/MerchantOrders/GetMerchantPublicOrders")
            .post(requestBody)
            .header("content-type","application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                orderResponse.getFailure(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                orderResponse.getResponse( response.body?.string()!!)
            }
        })

    }
    fun test(context: Context,token:String,orderResponse: OrderResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token",token)
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(Constant.API_URL + "api/user/userinfo?")
            .get()
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .build()
        Log.d("Jack",Constant.API_URL + "api/user/userinfo?");

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