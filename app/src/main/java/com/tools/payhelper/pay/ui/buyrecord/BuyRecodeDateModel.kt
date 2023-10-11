package com.jingyu.pay.ui.buyrecord

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

class BuyRecodeDateModel {


    var BaseUrl : String = Constant.API_URL
    fun getBuyRecordList(context: Context,date :String,accountCahngeResponse: AccountCahngeResponse){

        var jsonObject= JSONObject()
        jsonObject.put("token","")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()


        val urlBuilder: HttpUrl.Builder = (BaseUrl + "api/user/payment?").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("date", date)
        val url: String = urlBuilder.build().toString()

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
                accountCahngeResponse.getResponse( response.body?.string()!!)
            }
        })

    }

    interface AccountCahngeResponse{
        fun getResponse(s : String)
    }
}