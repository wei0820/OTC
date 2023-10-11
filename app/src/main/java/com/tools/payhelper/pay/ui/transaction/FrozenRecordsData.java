package com.tools.payhelper.pay.ui.transaction;

import com.google.gson.annotations.SerializedName;

public class FrozenRecordsData {
    @SerializedName("code")  public  int code;
    @SerializedName("msg")  public  String msg;
    @SerializedName("data") public Data[] data;

    public class  Data{
        @SerializedName("id")  public  String id;
        @SerializedName("accountId")  public  String accountId;
        @SerializedName("score")  public  double score;
        @SerializedName("sourceId")  public  String sourceId;
        @SerializedName("type")  public  String type;
        @SerializedName("tag")  public  String tag;
        @SerializedName("resoult")  public  String resoult;
        @SerializedName("isEnable")  public  boolean isEnable;
        @SerializedName("remark")  public  String remark;
        @SerializedName("created")  public  String created;
        @SerializedName("updated")  public  String updated;

    }

}
