package com.tools.payhelper.pay.ui.group;

import com.google.gson.annotations.SerializedName;

public class ReportsData {
    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;
    @SerializedName("data") public  Data data;

    public class  Data{
        @SerializedName("collection") public  double collection;
        @SerializedName("payment") public  double payment;
        @SerializedName("commission") public  double commission;
        @SerializedName("paymentXe") public  double paymentXe;
        @SerializedName("paymentXeCom") public  double paymentXeCom;
        @SerializedName("paymentXeQty") public  double paymentXeQty;


    }


}
