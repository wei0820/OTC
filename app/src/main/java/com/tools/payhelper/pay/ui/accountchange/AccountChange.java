package com.tools.payhelper.pay.ui.accountchange;

import com.google.gson.annotations.SerializedName;

public class AccountChange {

    @SerializedName("code") public  int code;
    @SerializedName("msg") public String msg;
    @SerializedName("data") public  Data[] data;

    public class  Data{

        @SerializedName("id")  public  String  id;
        @SerializedName("loginId") public  String loginId;
        @SerializedName("score") public  Double score;
        @SerializedName("remark") public  String remark;
        @SerializedName("quota")  public  Double quota;
        @SerializedName("quotaEnd") public  Double quotaEnd;
        @SerializedName("created")  public  String  created;
        @SerializedName("sourceId") public  String  sourceId;
        @SerializedName("tag")  public  String tag;
    }

}
