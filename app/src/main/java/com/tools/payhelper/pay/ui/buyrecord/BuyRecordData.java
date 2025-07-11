package com.tools.payhelper.pay.ui.buyrecord;

import com.google.gson.annotations.SerializedName;
import com.tools.payhelper.pay.Constant;

public class BuyRecordData {

    @SerializedName("code")
    public int code = Constant.DefaultValue_Int;
    @SerializedName("msg")
    public String msg = Constant.DefaultValue_String;
    @SerializedName("data")
    public Data[] data = null;

    public class Data {
        @SerializedName("id") public String id = Constant.DefaultValue_String;
        @SerializedName("orderNo") public String orderNo= Constant.DefaultValue_String;
        @SerializedName("bankName") public String bankName= Constant.DefaultValue_String;
        @SerializedName("subName") public String subName= Constant.DefaultValue_String;
        @SerializedName("userName") public String userName= Constant.DefaultValue_String;
        @SerializedName("cardId") public String cardId= Constant.DefaultValue_String;
        @SerializedName("paymentOrderId") public String paymentOrderId;
        @SerializedName("paymentQueueId") public String paymentQueueId;
        @SerializedName("accountId") public String accountId;
        @SerializedName("state") public Double state = Constant.DefaultValue_Double;
        @SerializedName("isEnable") public  boolean isEnable = Constant.DefaultValue_boolean;
        @SerializedName("commission")public Double commission = Constant.DefaultValue_Double;
        @SerializedName("score") public Double score = Constant.DefaultValue_Double;
        @SerializedName("remark") public String remark= Constant.DefaultValue_String;
        @SerializedName("created") public String created= Constant.DefaultValue_String;

    }


}
