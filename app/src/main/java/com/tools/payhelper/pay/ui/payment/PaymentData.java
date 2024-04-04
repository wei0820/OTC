package com.tools.payhelper.pay.ui.payment;

import com.google.gson.annotations.SerializedName;

public class PaymentData {
    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;
    @SerializedName("data") public Data[] data;

    public class Data{
        @SerializedName("id") public  String  id;
        @SerializedName("orderNo") public  String orderNo;
        @SerializedName("bankName") public  String bankName;
        @SerializedName("subName") public  String subName;
        @SerializedName("userName") public  String userName;
        @SerializedName("cardId") public  String cardId;
        @SerializedName("state") public  int state;
        @SerializedName("score") public  Double score;
        @SerializedName("remark") public  String remark;
        @SerializedName("created") public  String created;


    }

}
