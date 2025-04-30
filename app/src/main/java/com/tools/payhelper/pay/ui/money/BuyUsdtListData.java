package com.tools.payhelper.pay.ui.money;

import com.google.gson.annotations.SerializedName;

public class BuyUsdtListData {
    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;

    @SerializedName("data") public Data[] data;
    public class Data{
        @SerializedName("id") public  String  id;
        @SerializedName("orderno") public  String orderno;
        @SerializedName("hashvalue") public  String hashvalue;
        @SerializedName("scoreusdt") public  Double scoreusdt;
        @SerializedName("score") public  Double score;
        @SerializedName("exgrate") public  Double exgrate;


        @SerializedName("state") public  int state;
        @SerializedName("remark") public  String remark;
        @SerializedName("created") public  String created;

    }


}
