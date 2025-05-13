package com.tools.payhelper.pay.ui.home;

import com.google.gson.annotations.SerializedName;
import com.tools.payhelper.pay.Constant;

public class PaymentIsValidData {

    @SerializedName("code") public  int code = Constant.DefaultValue_Int;
    @SerializedName("msg") public  String msg = Constant.DefaultValue_String ;
}
