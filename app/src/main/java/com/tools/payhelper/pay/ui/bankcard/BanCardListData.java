package com.tools.payhelper.pay.ui.bankcard;

import com.google.gson.annotations.SerializedName;

public class BanCardListData {

    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;
    @SerializedName("data") public  Data[] data;
    public class Data{
        @SerializedName("id") public  String id;
        @SerializedName("accountId") public  String accountId;
        @SerializedName("bankName") public  String bankName;
        @SerializedName("subName") public  String subName;
        @SerializedName("userName") public  String userName;
        @SerializedName("pinYin") public  String pinYin;
        @SerializedName("cardNo") public  String cardNo;
        @SerializedName("collection") public  double collection;
        @SerializedName("collectionlimit") public  double collectionlimit;
        @SerializedName("collectionQty") public  double collectionQty;
        @SerializedName("isEnable") public  boolean isEnable;
        @SerializedName("created") public  String created;
    }
}
