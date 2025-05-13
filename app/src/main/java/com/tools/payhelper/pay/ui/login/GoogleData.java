package com.tools.payhelper.pay.ui.login;

import com.google.gson.annotations.SerializedName;

public class GoogleData {

    @SerializedName("msg")
    public  String msg = "Unknown";

    @SerializedName("code")
    public  int code = 1;
    @SerializedName("count")
    public  int count;
    @SerializedName("data")
    public Data data = null;

    public class  Data{
        @SerializedName("account")
        public  String account = "Unknown";
        @SerializedName("accountSecretKey")
        public  String accountSecretKey = "Unknown";
        @SerializedName("qrCodeSetupImageUrl")
        public  String qrCodeSetupImageUrl = "Unknown";
    }
}
