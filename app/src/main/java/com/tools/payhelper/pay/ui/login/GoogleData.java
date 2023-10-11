package com.tools.payhelper.pay.ui.login;

import com.google.gson.annotations.SerializedName;

public class GoogleData {

    @SerializedName("msg")
    public  String msg;

    @SerializedName("code")
    public  int code;
    @SerializedName("count")
    public  int count;
    @SerializedName("data")
    public Data data;

    public class  Data{
        @SerializedName("account")
        public  String account;
        @SerializedName("accountSecretKey")
        public  String accountSecretKey;
        @SerializedName("qrCodeSetupImageUrl")
        public  String qrCodeSetupImageUrl;
    }
}
