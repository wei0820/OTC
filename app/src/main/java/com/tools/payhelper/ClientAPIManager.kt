package com.tools.payhelper

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ClientAPIManager {
    var BaseUrl : String = BuildConfig.API_DOMAIN
    // 登入
    fun  setAnalyzing(title: String,publisherName : String ,publisherCode : String,message : String,clientResponse: ClientResponse){
        var jsonObject= JSONObject()
        jsonObject.put("title",title)
        jsonObject.put("publisherName",publisherName)
        jsonObject.put("publisherCode",publisherCode)
        jsonObject.put("message",message)


        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()

        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(BaseUrl + "/api/v1/Client/analyzing")
            .post(requestBody)
            .header("content-type","application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                clientResponse.getFailure(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                clientResponse.getResponse( response.body?.string()!!)
            }
        })

    }
    fun  setTraceLog(traceId: String,body : String ,clientResponse: ClientResponse){
        var jsonObject= JSONObject()
        jsonObject.put("traceId",traceId)
        jsonObject.put("body",body)



        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()

        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(BaseUrl + "/api/v1/Client/traceLog")
            .post(requestBody)
            .header("content-type","application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                clientResponse.getFailure(e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                clientResponse.getResponse( response.body?.string()!!)
            }
        })

    }

    interface  ClientResponse{
        fun getResponse(s : String)
        fun getFailure(s: String)
    }
}