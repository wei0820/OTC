package com.jingyu.pay.ui.dashboard

import android.content.Context
import android.util.Log
import com.jingyu.pay.ui.home.HomeDateModel
import com.jingyu.pay.ui.login.LoginDateModel
import com.tools.payhelper.pay.Constant
import com.tools.payhelper.pay.PayHelperUtils

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class SellDateModel {


    var BaseUrl : String = Constant.API_URL
    fun setSellSetting(context: Context, sellResponse: SellResponse){
        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(BaseUrl + "api/user/CollectionQueue")
            .get()
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .header("content-type","application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                sellResponse.getResponse( response.body?.string()!!)
            }
        })

    }


    fun getSellList(context: Context, sellResponse: SellResponse){
        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(BaseUrl + "api/user/collectioning")
            .get()
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .header("content-type","application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                sellResponse.getResponse( response.body?.string()!!)
            }
        })

    }

    fun setCloseSellSetting(context: Context, sellResponse: SellResponse){
        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(BaseUrl + "api/user/CollectionQueueOff")
            .get()
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .header("content-type","application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                sellResponse.getResponse( response.body?.string()!!)
            }
        })

    }


    fun setConfirmOrder(id : String , userName : String,context: Context, sellResponse: SellResponse){
        var jsonObject= JSONObject()
        jsonObject.put("id",id)
        jsonObject.put("userName",userName)
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(BaseUrl + "api/user/confirm")
            .post(requestBody)
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .header("content-type","application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                sellResponse.getResponse( response.body?.string()!!)
            }
        })

    }



    fun getExrate(context: Context, orderResponse: SellResponse){
        var jsonObject= JSONObject()
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(Constant.exrate_String)
            .get()
            .header("content-type","application/json")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                orderResponse.getResponse( response.body?.string()!!)
            }
        })
    }
    fun getUserinfo(context: Context,sellResponse: SellResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token", PayHelperUtils.getUserToken(context))
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
                sellResponse.getResponse( response.body?.string()!!)
            }
        })

    }






    interface SellResponse{
        fun getResponse(s : String)
    }
}