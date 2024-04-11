package com.tools.payhelper.pay.ui.login

import kotlinx.coroutines.flow.Flow

interface LoginFlowHelper {
    fun getLoginData() : Flow<ArrayList<Login>>

}