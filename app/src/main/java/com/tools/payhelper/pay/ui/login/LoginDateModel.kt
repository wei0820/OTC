package com.jingyu.pay.ui.login

import android.app.Activity
import android.util.Log
import com.google.firebase.database.core.Context
import com.tools.payhelper.pay.Constant
import com.tools.payhelper.pay.PayHelperUtils
import kotlinx.coroutines.channels.awaitClose
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
class LoginDateModel {

    fun setUserLogin(loginid:String,password:String,code:String,loginrResponse: LoginrResponse){
        var jsonObject= JSONObject()
        jsonObject.put("loginid",loginid)
        jsonObject.put("password",password)
        jsonObject.put("code",code)
        jsonObject.put("roleName","会员")
        jsonObject.put("IP","125.119.224.148")
        jsonObject.put("version","v8")
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(Constant.API_URL + "api/auth")
            .post(requestBody)
            .header("content-type","application/json")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                loginrResponse.getResponse( response.body?.string()!!)
            }
        })

    }

    fun setGoogleKey(activity: Activity,key:String,code:String,loginrResponse: LoginrResponse){
        var jsonObject= JSONObject()
        jsonObject.put("key",key)
        jsonObject.put("code",code)
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(Constant.API_URL + "api/auth/bindKey")
            .post(requestBody)
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(activity))

            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                loginrResponse.getResponse( response.body?.string()!!)
            }
        })

    }

    fun getGoogle(activity : Activity,loginrResponse: LoginrResponse){

        var jsonObject= JSONObject()
        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(Constant.API_URL + "api/auth/google")
            .get()
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(activity))

            .header("content-type","application/json")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Jack",e.localizedMessage)

            }

            override fun onResponse(call: Call, response: Response) {
                loginrResponse.getResponse( response.body?.string()!!)
            }
        })


    }



    fun getUpdate(): Flow<String> {

        return  callbackFlow{
            var jsonObject= JSONObject()
            var jsonStr=jsonObject.toString()
            val contentType: MediaType = "application/json".toMediaType()
            //调用请求
            val requestBody = jsonStr.toRequestBody(contentType)
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(Constant.UPDATE_URL)
                .get()
                .header("content-type","application/json")
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    offer(response.body?.string()!!)
                }
            })
            awaitClose {

            }
        }




    }


    interface LoginrResponse{
        fun getResponse(s : String)
    }
}