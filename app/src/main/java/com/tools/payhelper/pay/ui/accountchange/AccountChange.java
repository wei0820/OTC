package com.tools.payhelper.pay.ui.accountchange;

import com.google.gson.annotations.SerializedName;

public class AccountChange {

    @SerializedName("code") public  int code = 1 ;
    @SerializedName("msg") public String msg = "Unknown";
    @SerializedName("data") public  Data[] data = null;

    public class  Data{

        @SerializedName("id")  public  String  id = "Unknown";
        @SerializedName("loginId") public  String loginId= "Unknown";
        @SerializedName("score") public  Double score = 0.0 ;
        @SerializedName("remark") public  String remark = "Unknown";
        @SerializedName("quota")  public  Double quota = 0.0;
        @SerializedName("quotaEnd") public  Double quotaEnd = 0.0 ;
        @SerializedName("created")  public  String  created = "Unknown";
        @SerializedName("sourceId") public  String  sourceId = "Unknown";
        @SerializedName("tag")  public  String tag = "Unknown";
    }

}
