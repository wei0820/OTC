package com.tools.payhelper.pay.ui.money;

import com.google.gson.annotations.SerializedName;

public class UsdtinfoData {
    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;

    @SerializedName("data") public Data data;
    public class Data{
        @SerializedName("usdT_ADDR") public  String  usdT_ADDR;
        @SerializedName("usdT_EXRATE") public  double usdT_EXRATE;



    }


}
