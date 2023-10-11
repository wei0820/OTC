package com.tools.payhelper.pay.ui.group;

import com.google.gson.annotations.SerializedName;

public class ReportsTeamData {

    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;
    @SerializedName("data") public  Data[] data;

    public class  Data{
        @SerializedName("accountId") public  String accountId;
        @SerializedName("loginId") public  String loginId;
        @SerializedName("collection") public  double collection;
        @SerializedName("payment") public  double payment;
        @SerializedName("commission") public  double commission;
        @SerializedName("paymentXe") public  double paymentXe;
        @SerializedName("paymentXeQty") public  double paymentXeQty;
        @SerializedName("rebate") public  double rebate;
        @SerializedName("paymentXeRebate") public  double paymentXeRebate;


    }


}
