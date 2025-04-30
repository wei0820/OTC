package com.jingyu.pay.ui.notifications

import android.content.Context
import android.util.Log
import com.jingyu.pay.ui.dashboard.SellDateModel
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
    val callMap = HashMap<Long, Call>()

    fun setPostTransMoneyData(context: Context,loginId:String,score:Double,code:String,orderResponse: OrderResponse){
        var jsonObject= JSONObject()
        jsonObject.put("LoginId",loginId)
        jsonObject.put("Score",score)
        jsonObject.put("Code",code)

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
            .url(BaseUrl + "api/user/transfer")
            .post(requestBody)
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .build()
        Log.d("trans",BaseUrl + "api/user/transfer")
        Log.d("trans",jsonStr)
        val time  = System.currentTimeMillis()

        if (callMap.containsKey(time)) {
            val call = callMap[time]
            if (call != null && !call.isCanceled()) {
                call.cancel()
            }
        }


        val call = client.newCall(request)
        callMap.put(time,call)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                orderResponse.getFailure(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                orderResponse.getResponse( response.body?.string()!!)
            }
        })

    }

    fun getUserinfo(context: Context,orderResponse: OrderResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token", PayHelperUtils.getUserToken(context))
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
//        val client = OkHttpClient()
        val client = OkHttpClient.Builder()
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),SSLSocketClient.getX509TrustManager())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(BaseUrl + "api/user/transfer")
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
    fun getTransList(context: Context,orderResponse: PersonalDateModel.OrderResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
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
            .url(BaseUrl + "api/user/transfer")
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
    fun getUSDTInfo(context: Context,orderResponse: PersonalDateModel.OrderResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
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
            .url(BaseUrl + "api/user/USDTInfo")
            .get()
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .build()
        Log.d("usdt",BaseUrl + "api/user/USDTInfo");

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {


            }

            override fun onResponse(call: Call, response: Response) {
                orderResponse.getResponse( response.body?.string()!!)
            }
        })

    }

    fun setPostUsdtata(context: Context,score:Double,code:String,hashValue:String,orderResponse: OrderResponse){
        var jsonObject= JSONObject()
        jsonObject.put("Score",score)
        jsonObject.put("Code",code)
        jsonObject.put("HashValue",hashValue)



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
            .url(BaseUrl + "api/user/USDTTopup")
            .post(requestBody)
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .build()
        Log.d("trans",BaseUrl + "api/user/USDTTopup")
        Log.d("trans",jsonStr)
        val time  = System.currentTimeMillis()

        if (callMap.containsKey(time)) {
            val call = callMap[time]
            if (call != null && !call.isCanceled()) {
                call.cancel()
            }
        }


        val call = client.newCall(request)
        callMap.put(time,call)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                orderResponse.getFailure(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                orderResponse.getResponse( response.body?.string()!!)
            }
        })

    }
    fun getUSDTList(context: Context,orderResponse: PersonalDateModel.OrderResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
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
            .url(BaseUrl + "api/user/GetUSDTTopups")
            .get()
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .build()
        Log.d("usdt",BaseUrl + "api/user/GetUSDTTopups");

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