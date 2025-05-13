package com.tools.payhelper.pay.ui.bankcard;

import com.google.gson.annotations.SerializedName;
import com.tools.payhelper.pay.Constant;

public class AddBankCardData {

    @SerializedName("code") public  int code = Constant.DefaultValue_Int;
    @SerializedName("msg") public  String msg = Constant.DefaultValue_String;


}
