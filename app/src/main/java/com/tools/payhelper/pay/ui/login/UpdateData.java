package com.tools.payhelper.pay.ui.login;

import com.google.gson.annotations.SerializedName;

public class UpdateData {


    @SerializedName("msg")
    public  String msg;

    @SerializedName("code")
    public  int code;
    @SerializedName("data")
    public Data data;
    public class Data{
        @SerializedName("versionName")
        public  String versionName;
        @SerializedName("versionCode")
        public  Integer versionCode;
        @SerializedName("url")
        public String  url;

    }
}
