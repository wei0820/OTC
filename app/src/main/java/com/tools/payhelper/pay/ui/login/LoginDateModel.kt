package com.jingyu.pay.ui.login

import android.app.Activity
import android.content.Context
import android.util.Log
import com.jingyu.pay.ui.dashboard.SellDateModel
import com.tools.payhelper.SystemUtil
import com.tools.payhelper.pay.Constant
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ui.login.SSLSocketClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class LoginDateModel {
    var BaseUrl : String = Constant.API_URL
    val callMap = HashMap<Long, Call>()

    fun setUserLogin(context: Context,loginid:String,password:String,code:String,loginrResponse: LoginrResponse){
        var jsonObject= JSONObject()
        jsonObject.put("loginid",loginid)
        jsonObject.put("password",password)
        jsonObject.put("code",code)
        jsonObject.put("roleName","会员")
        jsonObject.put("IP",PayHelperUtils.getDeviceIP(context))
        jsonObject.put("version",Constant.versionnumber)
        jsonObject.put("ismobile","Android_"+PayHelperUtils.getVersionName()+"_userDevice_"+ SystemUtil.getUserDevice()+"_userAppName_"+Constant.apppakegename)

        var jsonStr=jsonObject.toString()
        val time  = System.currentTimeMillis()

        val contentType: MediaType = "application/json".toMediaType()
        if (callMap.containsKey(time)) {
            val call = callMap[time]
            if (call != null && !call.isCanceled()) {
                call.cancel()
            }
        }
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)

        val client = OkHttpClient.Builder()
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),SSLSocketClient.getX509TrustManager())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()



        val request = Request.Builder()
            .url(Constant.API_URL + "api/auth")
            .post(requestBody)
            .header("content-type","application/json")
            .build()

        val call = client.newCall(request)
        callMap.put(time,call)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (!e.localizedMessage.isEmpty()){
                    loginrResponse.getErrorResponse(e.localizedMessage)
                }



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
//        val client = OkHttpClient()
        val client = OkHttpClient.Builder()
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),SSLSocketClient.getX509TrustManager())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

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
//        val client = OkHttpClient()
        val client = OkHttpClient.Builder()
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),SSLSocketClient.getX509TrustManager())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
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
//            val client = OkHttpClient()
            val client = OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),SSLSocketClient.getX509TrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
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
//        val client = OkHttpClient()
        val client = OkHttpClient.Builder()
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),SSLSocketClient.getX509TrustManager())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
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

    fun getSellDataList(context: Context, loginrResponse: LoginrResponse){
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
            .url(BaseUrl + "api/user/collectioning")
            .get()
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .header("content-type","application/json")
            .build()
        Log.d("jack",BaseUrl + "api/user/collectioning")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                loginrResponse.getResponse( response.body?.string()!!)
            }
        })


    }

    fun postDRMBNotification(context: Context,FromAccount:String,ToAccount:String,Amount:String,FullText:String,loginrResponse: LoginrResponse){
        var jsonObject= JSONObject()
        jsonObject.put("FromAccount",FromAccount)
        jsonObject.put("ToAccount",ToAccount)
        jsonObject.put("Amount",Amount)
        jsonObject.put("FullText",FullText)

        var jsonStr=jsonObject.toString()
        val time  = System.currentTimeMillis()

        val contentType: MediaType = "application/json".toMediaType()
        if (callMap.containsKey(time)) {
            val call = callMap[time]
            if (call != null && !call.isCanceled()) {
                call.cancel()
            }
        }
        //调用请求
        val requestBody = jsonStr.toRequestBody(contentType)

        val client = OkHttpClient.Builder()
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),SSLSocketClient.getX509TrustManager())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()



        val request = Request.Builder()
            .url(Constant.API_URL + "api/user/DRMBNotification")
            .post(requestBody)
            .header("content-type","application/json")
            .header("Authorization", "Bearer " + PayHelperUtils.getUserToken(context))
            .build()
        Log.d("onReceiveMessage",Constant.API_URL + "api/DRMBNotification")

        val call = client.newCall(request)
        callMap.put(time,call)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (!e.localizedMessage.isEmpty()){
                    loginrResponse.getErrorResponse(e.localizedMessage)
                }



            }

            override fun onResponse(call: Call, response: Response) {
                loginrResponse.getResponse( response.body?.string()!!)
            }
        })

    }

    interface LoginrResponse{
        fun getResponse(s : String)
        fun getErrorResponse(s : String)
    }
}