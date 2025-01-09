package com.jingyu.pay.ui.group

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

class GroupDateModel {


    var BaseUrl : String = Constant.API_URL
    fun getGroupTimeList(context: Context, groupResponse: GroupResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/team?").toHttpUrlOrNull()!!.newBuilder()
//        urlBuilder.addQueryParameter("key", "")
//        urlBuilder.addQueryParameter("page", "")
        urlBuilder.addQueryParameter("pagesize", "99999")
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


    fun getGroupReportsList(context: Context, id : String,day : String,groupResponse: GroupResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/Reports?").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("id", id)
        urlBuilder.addQueryParameter("day", day)
        val url: String = urlBuilder.build().toString()
        Log.d("Jack",url);

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


    fun getGroupReportsTeamList(context: Context, id : String,day : String,groupResponse: GroupResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/ReportsTeam?").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("id", id)
        urlBuilder.addQueryParameter("day", day)
        val url: String = urlBuilder.build().toString()
        Log.d("Jack",url);

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


    fun getGroupReportDay(context: Context,groupResponse: GroupResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/ReportDay?").toHttpUrlOrNull()!!.newBuilder()
//        urlBuilder.addQueryParameter("id", "")
//        urlBuilder.addQueryParameter("day", "")
        val url: String = urlBuilder.build().toString()
        Log.d("Jack",url);

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



    fun getGroupRegister(context: Context,
                         LoginId:String,
                         PassWord:String,
                         Tel:String,
                         Rebate:Double,
                         PaymentXeRebate:Double,
                         AlipayRebate:Double,
                         wechatrebate: Double,
                         drmbRebate : Double,
                         unionRebate : Double,
                         AlipayXeRebate:Double,
                         wechaXetrebate: Double,
                         groupResponse: GroupResponse){

        var jsonObject= JSONObject()
        jsonObject.put("LoginId",LoginId)
        jsonObject.put("PassWord",PassWord)
        jsonObject.put("Tel",Tel)
        jsonObject.put("Rebate",Rebate)
        jsonObject.put("PaymentXeRebate",PaymentXeRebate)
        jsonObject.put("AlipayRebate",AlipayRebate)
        jsonObject.put("WeChatRebate",wechatrebate)
        jsonObject.put("AlipayXeRebate",AlipayXeRebate)
        jsonObject.put("WeChatXeRebate",wechaXetrebate)
        jsonObject.put("drmbRebate",drmbRebate)
        jsonObject.put("unionRebate",unionRebate)




        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/register?").toHttpUrlOrNull()!!.newBuilder()
//        urlBuilder.addQueryParameter("id", "")
//        urlBuilder.addQueryParameter("day", "")
        val url: String = urlBuilder.build().toString()
        Log.d("Jack",url);

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

    interface GroupResponse{
        fun getResponse(s : String)
    }
}