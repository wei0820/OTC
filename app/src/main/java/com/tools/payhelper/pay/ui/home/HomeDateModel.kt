package com.jingyu.pay.ui.home

import android.content.Context
import android.util.Log
import com.jingyu.pay.ui.login.LoginDateModel
import com.jingyu.pay.ui.order.OrderDateModel
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


class HomeDateModel {


    var BaseUrl : String = Constant.API_URL


    fun setBuySetting(context: Context,paymentlimit : String,paymentMax : String,orderResponse: BuyResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()


        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/PaymentQueue?").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("Paymentlimit", paymentlimit)
        urlBuilder.addQueryParameter("PaymentMax", paymentMax)


        val url: String = urlBuilder.build().toString()
        Log.d("Jack",url)

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
                Log.d("Jack",e.toString());


            }

            override fun onResponse(call: Call, response: Response) {
                orderResponse.getResponse( response.body?.string()!!)
            }
        })

    }

    fun getUserinfo(context: Context, orderResponse: BuyResponse){

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


    fun getPayment(context: Context,id : String,orderResponse: BuyResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()


        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/PaymentMatching?").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("id", id)


        val url: String = urlBuilder.build().toString()

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





    fun getBuyOrederList(context: Context,orderResponse: BuyResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
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
            .url(BaseUrl + "api/user/pendingPush?")
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

    fun getPaymentMatching(context: Context, orderResponse: OrderDateModel.OrderResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
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


    fun getExrate(context: Context, orderResponse: BuyResponse){
        var jsonObject= JSONObject()
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


    fun getPaymentIsValid(context: Context,id:String, orderResponse: BuyResponse){
        var jsonObject= JSONObject()
        jsonObject.put("id",id)
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
            .url(BaseUrl + "api/user/PaymentIsValid")
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






    interface BuyResponse{
        fun getResponse(s : String)
        fun getFailure(s: String)
    }
}