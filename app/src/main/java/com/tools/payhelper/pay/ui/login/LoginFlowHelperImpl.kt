package com.tools.payhelper.pay.ui.login

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginFlowHelperImpl(private  val  loginService: LoginService,var id: String,var password: String,var code:String): LoginFlowHelper {
    override fun getLoginData() = flow {
        emit(loginService.getLoginData(LoginPostData(id,password,code )))
    }
}