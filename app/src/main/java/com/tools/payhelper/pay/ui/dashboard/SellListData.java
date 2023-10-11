package com.tools.payhelper.pay.ui.dashboard;

import com.google.gson.annotations.SerializedName;

public class SellListData {

    @SerializedName("code") public  int code;

    @SerializedName("msg") public  String msg;
    @SerializedName("data") public  Data[] data;
    public class Data{
        @SerializedName("id") public  String id;
        @SerializedName("orderNo") public  String orderNo;
        @SerializedName("payUserName") public  String payUserName;
        @SerializedName("collectionQueueId") public  String collectionQueueId;
        @SerializedName("accountId") public  String accountId;
        @SerializedName("state") public  int state;
        @SerializedName("isEnable") public  boolean isEnable;
        @SerializedName("commission") public  double commission;
        @SerializedName("score") public  double score;
        @SerializedName("remark") public  String remark;
        @SerializedName("created") public  String created;
        @SerializedName("userName") public  String userName;
        @SerializedName("cardNo") public  String cardNo;
        @SerializedName("bankName") public  String bankName;

    }




}
