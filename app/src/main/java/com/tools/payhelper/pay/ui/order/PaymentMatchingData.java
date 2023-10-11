package com.tools.payhelper.pay.ui.order;

import com.google.gson.annotations.SerializedName;

public class PaymentMatchingData {

    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;
    @SerializedName("data") public  Data[] data;

    public  class  Data {
        @SerializedName("id") public  String id;
        @SerializedName("bankName") public  String bankName;
        @SerializedName("cardId") public  String cardId;
        @SerializedName("subName") public  String subName;
        @SerializedName("userName") public  String userName;
        @SerializedName("score") public  String score;
        @SerializedName("orderNo") public  String orderNo;
        @SerializedName("state") public  int state;
        @SerializedName("isEnable") public  Boolean isEnable;
        @SerializedName("created") public  String created;
        @SerializedName("remark") public  String remark;


    }
}
