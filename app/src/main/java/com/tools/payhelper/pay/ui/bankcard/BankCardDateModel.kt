package com.jingyu.pay.ui.bankcard

import android.content.Context
import android.util.Log
import com.tools.payhelper.pay.Constant
import com.tools.payhelper.pay.PayHelperUtils

import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class BankCardDateModel {


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
        val client = OkHttpClient()
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


    fun getGroupReportsList(context: Context, id : String,day : String,groupResponse: BankCardResponse){

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
        val client = OkHttpClient()
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




    fun setBankCard(context: Context,bankName:String,
                    subName:String,
                    cardNo:String,
                    collectionlimit:Float,
                    code:String,
                    userName:String,
                    PinYin:String,
                         groupResponse: BankCardResponse){

        var jsonObject= JSONObject()
        jsonObject.put("bankName",bankName)
        jsonObject.put("subName",subName)
        jsonObject.put("cardNo",cardNo)
        jsonObject.put("IsEnable",false)
        jsonObject.put("collectionlimit",collectionlimit)
        jsonObject.put("code",code)
        jsonObject.put("userName",userName)
        jsonObject.put("id","")
        jsonObject.put("PinYin",PinYin)


        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/bindCard").toHttpUrlOrNull()!!.newBuilder()
//        urlBuilder.addQueryParameter("id", "")
//        urlBuilder.addQueryParameter("day", "")
        val url: String = urlBuilder.build().toString()
        Log.d("Jack",url);

        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
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


    fun setStopBankCard(context: Context, id : String,groupResponse: BankCardResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/cardon?").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("id", id)
        val url: String = urlBuilder.build().toString()
        Log.d("Jack",url);

        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
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


    fun setDeleteBankCard(context: Context,id: String,groupResponse: BankCardResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/cardremove?").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("id", id)
//        urlBuilder.addQueryParameter("day", "")
        val url: String = urlBuilder.build().toString()
        Log.d("Jack",url);

        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
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