package com.tools.payhelper.pay.ui.login

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("msg") var msg: String,
    @SerializedName("code") var code: Int ,
    @SerializedName("data") var data : Data

    )

data class Data(
    @SerializedName("token")  var token: String ,
    @SerializedName("google") var google : Boolean

)
