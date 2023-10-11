package com.tools.payhelper.pay.ui.group;

import com.google.gson.annotations.SerializedName;

public class GroupListData {

    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;
    @SerializedName("data") public  Data [] data;

    public class Data{
        @SerializedName("id") public  String id;
        @SerializedName("loginId") public  String loginId;
        @SerializedName("apis") public  String apis;
        @SerializedName("mobile") public  String mobile;
        @SerializedName("rebate") public  double rebate;
        @SerializedName("paymentXeRebate") public  double paymentXeRebate;
        @SerializedName("isPayment") public  boolean isPayment;
        @SerializedName("isCollection") public  boolean isCollection;
        @SerializedName("isEnable") public  boolean isEnable;
        @SerializedName("pid") public  String pid;
        @SerializedName("quota") public  double quota;
        @SerializedName("frozen") public  double frozen;
        @SerializedName("bond") public  double bond;
        @SerializedName("commission") public  double commission;
        @SerializedName("created") public  String created;
        @SerializedName("alipayRebate") public  String alipayRebate;




    }


}
