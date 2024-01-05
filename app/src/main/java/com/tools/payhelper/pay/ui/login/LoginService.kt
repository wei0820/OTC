package com.tools.payhelper.pay.ui.login

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Content-Type: application/json")
    @POST("api/auth")
    suspend fun getLoginData(@Body loginPostData: LoginPostData): ArrayList<Login>
}