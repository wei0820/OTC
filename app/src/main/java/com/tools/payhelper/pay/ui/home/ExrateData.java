package com.tools.payhelper.pay.ui.home;

import com.google.gson.annotations.SerializedName;

public class ExrateData {

    @SerializedName("code") public  int code ;
    @SerializedName("msg") public  String msg ;
    @SerializedName("data") public  Data data ;

    public class Data{
        @SerializedName("exrate") public  Double exrateDoubel;

    }


}
