package com.tools.payhelper.pay.ui.login;

import android.content.Context;
import android.provider.Settings;

public class TelephonyManager {
     public static  String id ="null";
    public static String  getandroID(Context context){

        try {
            id =  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        }catch (Exception e){
            id = "null";

        }finally {
            id = "null";

        }


        return  id;
    }

}
