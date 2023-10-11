package com.tools.payhelper.pay.ui.group;

import com.google.gson.annotations.SerializedName;

public class ReportDayData {

    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;
    @SerializedName("data") public  Data[] data;

    public class  Data{
        @SerializedName("id") public  String id;
        @SerializedName("accountId") public  String accountId;
        @SerializedName("score") public  double score;
        @SerializedName("tag") public  double tag;
        @SerializedName("sourceId") public  String sourceId;
        @SerializedName("created") public  String created;
    }


}
