package com.tools.payhelper.pay.ui.money;

import com.google.gson.annotations.SerializedName;
import com.tools.payhelper.pay.ui.payment.PaymentData;

public class TransferListData {
    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;

    @SerializedName("data") public Data[] data;
    public class Data{
        @SerializedName("id") public  String  id;
        @SerializedName("state") public  int state;
        @SerializedName("score") public  Double score;
        @SerializedName("inOut") public  boolean inOut;
        @SerializedName("remark") public  String remark;
        @SerializedName("created") public  String created;


    }

}
