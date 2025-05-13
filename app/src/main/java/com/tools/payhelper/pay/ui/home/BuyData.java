package com.tools.payhelper.pay.ui.home;

import com.google.gson.annotations.SerializedName;
import com.tools.payhelper.pay.Constant;

public class BuyData {
    @SerializedName("code") public  int code = Constant.DefaultValue_Int;
    @SerializedName("msg") public  String msg = Constant.DefaultValue_String;
    @SerializedName("data") public  Data []data = null;
    public class Data{
        @SerializedName("id") public  String id = Constant.DefaultValue_String ;
        @SerializedName("orderNo") public  String orderNo = Constant.DefaultValue_String ;
        @SerializedName("bankName") public  String bankName = Constant.DefaultValue_String ;
        @SerializedName("subName") public  String subName = Constant.DefaultValue_String ;
        @SerializedName("userName") public  String userName = Constant.DefaultValue_String ;
        @SerializedName("cardId") public  String cardId = Constant.DefaultValue_String ;
        @SerializedName("state") public  String state = Constant.DefaultValue_String ;
        @SerializedName("score") public  Double score = Constant.DefaultValue_Double ;
        @SerializedName("created") public  String created = Constant.DefaultValue_String ;
        @SerializedName("ordertype") public  String ordertype = Constant.DefaultValue_String ;


    }

}
