package com.jingyu.pay.ui.bankcard

import android.content.Context
import android.util.Log
import com.tools.payhelper.pay.Constant
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ui.login.SSLSocketClient

import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class PaymentDateModel {


    var BaseUrl : String = Constant.API_URL
    fun getBankCradList(context: Context, groupResponse: BankCardResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/cards").toHttpUrlOrNull()!!.newBuilder()
//        urlBuilder.addQueryParameter("key", "")
//        urlBuilder.addQueryParameter("page", "")
//        urlBuilder.addQueryParameter("pagesize", "")
        val url: String = urlBuilder.build().toString()
        Log.d("Jack",url);

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
            .url(url)
            .get()
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                groupResponse.getResponse( response.body?.string()!!)
            }
        })

    }


    fun setPayment(
        context: Context,
        bankName: String,
        subName: String,
        orderNo: String,
        cardId: String,
        code: String,
        userName: String,
        score: Float,
        groupResponse: BankCardResponse,
    ){

        var jsonObject= JSONObject()
        jsonObject.put("bankName",bankName)
        jsonObject.put("subName",subName)
        jsonObject.put("orderNo",orderNo)
        jsonObject.put("code",code)
        jsonObject.put("userName",userName)
        jsonObject.put("cardId",cardId)
        jsonObject.put("score",score)


        var jsonStr=jsonObject.toString()
        Log.d("jack", jsonStr)

        val contentType: MediaType = "application/json".toMediaType()
        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/payment").toHttpUrlOrNull()!!.newBuilder()
//        urlBuilder.addQueryParameter("id", "")
//        urlBuilder.addQueryParameter("day", "")
        val url: String = urlBuilder.build().toString()
        Log.d("jack", url)

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
            .url(url)
            .post(requestBody)
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                groupResponse.getResponse( response.body?.string()!!)
            }
        })

    }
    fun getPaymentList(context: Context, groupResponse: BankCardResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/accountOrder").toHttpUrlOrNull()!!.newBuilder()
//        urlBuilder.addQueryParameter("key", "")
//        urlBuilder.addQueryParameter("page", "")
//        urlBuilder.addQueryParameter("pagesize", "")
        val url: String = urlBuilder.build().toString()
        Log.d("Jack",url);

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
            .url(url)
            .get()
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                groupResponse.getResponse( response.body?.string()!!)
            }
        })

    }







    interface BankCardResponse{
        fun getResponse(s : String)
    }
}