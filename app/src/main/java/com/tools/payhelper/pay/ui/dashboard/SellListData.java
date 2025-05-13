package com.tools.payhelper.pay.ui.dashboard;

import com.google.gson.annotations.SerializedName;

public class SellListData {

    @SerializedName("code") public  int code = 1;

    @SerializedName("msg") public  String msg = "Unknown";
    @SerializedName("data") public  Data[] data = null;
    public class Data{
        @SerializedName("id") public  String id = "Unknown";
        @SerializedName("orderNo") public  String orderNo = "Unknown";
        @SerializedName("payUserName") public  String payUserName;
        @SerializedName("collectionQueueId") public  String collectionQueueId= "Unknown";
        @SerializedName("accountId") public  String accountId= "Unknown";
        @SerializedName("state") public  int state = 0;
        @SerializedName("isEnable") public  boolean isEnable = false;
        @SerializedName("commission") public  double commission = 0.0;
        @SerializedName("score") public  double score = 0.0;
        @SerializedName("remark") public  String remark = "Unknown";
        @SerializedName("created") public  String created = "Unknown";
        @SerializedName("userName") public  String userName = "Unknown";
        @SerializedName("cardNo") public  String cardNo = "Unknown";
        @SerializedName("bankName") public  String bankName = "Unknown";

    }




}
