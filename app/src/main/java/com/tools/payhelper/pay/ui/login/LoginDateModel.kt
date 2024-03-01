package com.jingyu.pay.ui.login

import android.app.Activity
import android.util.Log
import com.tools.payhelper.pay.Constant
import android.content.Context
import com.tools.payhelper.pay.PayHelperUtils
import kotlinx.coroutines.channels.awaitClose
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit


class LoginDateModel {

    fun setUserLogin(loginid:String,password:String,code:String,loginrResponse: LoginrResponse){
        var jsonObject= JSONObject()
        jsonObject.put("loginid",loginid)
        jsonObject.put("password",password)
        jsonObject.put("code",code)
        jsonObject.put("roleName","会员")
        jsonObject.put("IP","125.119.224.148")
        jsonObject.put("version","v8")
        jsonObject.put("ismobile",PayHelperUtils.getVersionName())

        var jsonStr=jsonObject.toString()
        val contentType: MediaType = "application/json".toMediaType()

        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)
//        val client = OkHttpClient()

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


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
        Log.d("Jack",Constant.API_URL + "api/auth/bindKey")

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
        Log.d("Jack",Constant.API_URL + "api/auth/google")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                loginrResponse.getResponse( response.body?.string()!!)
            }
        })


    }
    fun getUserinfo(context: Context, loginrResponse: LoginrResponse){

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


    fun getVersionUpdate(context: Context, loginrResponse: LoginrResponse) {

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
                loginrResponse.getResponse( response.body?.string()!!)

            }
        })

    }

    interface LoginrResponse{
        fun getResponse(s : String)
    }
}