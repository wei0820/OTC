package com.jingyu.pay.ui.transaction

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

class FrozenRecordsDateModel {


    var BaseUrl : String = Constant.API_URL

    fun getFrozenRecordsData(context: Context,date: String, orderResponse: OrderResponse){
        Log.d("Jack",date);

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()


        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/FrozenRecords?").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("date", date)
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
                orderResponse.getResponse( response.body?.string()!!)
            }
        })

    }


    interface OrderResponse{
        fun getResponse(s : String)
        fun getFailure(s: String)
    }
}