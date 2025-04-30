package com.tools.payhelper.pay.ui.news;

import com.google.gson.annotations.SerializedName;

public class NewsListData {
    @SerializedName("code") public  int code;
    @SerializedName("msg") public  String msg;

    @SerializedName("data") public Data[] data;
    public class Data{
        @SerializedName("isTop") public  boolean  isTop;
        @SerializedName("text") public  String istext;
        @SerializedName("applyDate") public  String SapplyDate;


    }

}
