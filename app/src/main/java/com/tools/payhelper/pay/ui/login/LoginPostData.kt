package com.tools.payhelper.pay.ui.login

data class LoginPostData(
    var loginid : String,
    var password: String,
    var code : String,
    var roleName : String = "会员",
    var IP :String = "125.119.224.148",
    var version : String = "v8"



)
