package com.tools.payhelper.pay

import android.content.Context
import com.google.gson.Gson
import com.tools.payhelper.pay.ui.order.PaymentMatchingData

class TestManager {
    var array = ArrayList<PaymentMatchingData.Data>()

    suspend fun test(context: Context) :ArrayList<PaymentMatchingData.Data>{

        return  array

    }
}