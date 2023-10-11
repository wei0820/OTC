package com.tools.payhelper.pay.ui.notifications;

import com.google.gson.annotations.SerializedName;

public class UserinfoData {
    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;
    @SerializedName("count") public  int count;
    @SerializedName("data") public  Data data;

    public class Data{
        @SerializedName("rebate") public  float rebate;
        @SerializedName("paymentXeRebate") public  float paymentXeRebate;
        @SerializedName("alipayRebate") public  float alipayRebate;

        @SerializedName("quota") public  float quota;
        @SerializedName("frozen") public  float frozen;
        @SerializedName("payment") public  float payment;
        @SerializedName("collection") public  float collection;
        @SerializedName("commission") public  float commission;
        @SerializedName("isPayment") public  boolean isPayment;
        @SerializedName("isCollection") public  boolean isCollection;
        @SerializedName("isEnable") public  boolean isEnable;
        @SerializedName("isSecret") public  boolean isSecret;
        @SerializedName("isVIP") public  boolean isVIP;
        @SerializedName("cardQty") public  int cardQty;
        @SerializedName("note") public  String note;
        @SerializedName("apIs") public  String apIs;
        @SerializedName("index") public  int index;

    }



}
