package com.tools.payhelper.pay.ui.login;

import com.google.gson.annotations.SerializedName;

public class UpdateData {


    @SerializedName("msg")
    public  String msg = "Unknown";

    @SerializedName("code")
    public  int code = 1;
    @SerializedName("data")
    public Data data = null;
    public class Data{
        @SerializedName("versionName")
        public  String versionName = "Unknown";
        @SerializedName("versionCode")
        public  Integer versionCode = 0;
        @SerializedName("url")
        public String  url = "Unknown";
        @SerializedName("quality")
        public int  quality = 0;
    }
}
