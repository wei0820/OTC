package com.tools.payhelper.pay.ui.login;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("msg")
    public  String msg;

    @SerializedName("code")
    public  int code;
    @SerializedName("count")
    public  int count;
    @SerializedName("data")
    public Data data;
    public class Data{
        @SerializedName("token")
        public  String token;
        @SerializedName("google")
        public boolean google;

    }
}
