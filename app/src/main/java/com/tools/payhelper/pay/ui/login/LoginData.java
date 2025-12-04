package com.tools.payhelper.pay.ui.login;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("msg")
    public  String msg = "Unknown";

    @SerializedName("code")
    public  int code = 1;
    @SerializedName("count")
    public  int count;
    @SerializedName("data")
    public Data data = null;
    public class Data{
        @SerializedName("token")
        public  String token = "Unknown";
        @SerializedName("google")
        public boolean google = true ;

    }
}
