package com.tools.payhelper.pay.ui.bankcard;

import com.google.gson.annotations.SerializedName;
import com.tools.payhelper.pay.Constant;

public class BanCardListData {

    @SerializedName("code") public  int code = Constant.DefaultValue_Int;
    @SerializedName("msg") public  String msg = Constant.DefaultValue_String;
    @SerializedName("data") public  Data[] data = null;
    public class Data{
        @SerializedName("id") public  String id = Constant.DefaultValue_String;
        @SerializedName("accountId") public  String accountId  = Constant.DefaultValue_String;
        @SerializedName("bankName") public  String bankName = Constant.DefaultValue_String;
        @SerializedName("subName") public  String subName = Constant.DefaultValue_String;
        @SerializedName("userName") public  String userName = Constant.DefaultValue_String;
        @SerializedName("pinYin") public  String pinYin = Constant.DefaultValue_String;
        @SerializedName("cardNo") public  String cardNo = Constant.DefaultValue_String;
        @SerializedName("collection") public  double collection = Constant.DefaultValue_Double;
        @SerializedName("collectionlimit") public  double collectionlimit = Constant.DefaultValue_Double;
        @SerializedName("collectionQty") public  double collectionQty = Constant.DefaultValue_Double;
        @SerializedName("isEnable") public  boolean isEnable = Constant.DefaultValue_boolean;
        @SerializedName("created") public  String created = Constant.DefaultValue_String;
        @SerializedName("lock") public  String lock  = Constant.DefaultValue_String;
        @SerializedName("isAWXe") public  boolean isAWXe = Constant.DefaultValue_boolean;

        @SerializedName("minAmount") public  double minAmount = Constant.DefaultValue_Double;

        @SerializedName("maxAmount") public  double maxAmount = Constant.DefaultValue_Double;

    }
}
