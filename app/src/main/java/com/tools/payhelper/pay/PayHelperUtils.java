package com.tools.payhelper.pay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tools.payhelper.BuildConfig;
import com.tools.payhelper.pay.ui.bankcard.BanCardListData;
import com.tools.payhelper.pay.ui.home.BuyData;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PayHelperUtils {


    public static  String getLocalIpAddress(Context context) {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.d("Jack", "***** IP="+ ip);
                        PayHelperUtils.saveDeviceIP(context,ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.d("Jack", ex.toString());
        }
        return null;
    }

    public static Integer getVersionCode(){
        return  BuildConfig.VERSION_CODE;

    }

    public static String getVersionName(){
        return BuildConfig.VERSION_NAME;

    }


    public static void copyToClipboard(Context context,String str){
        ToastManager.showToastCenter(context,"已复制:"+str);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
            Log.e("version","1 version");
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager )context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label",str);
            clipboard.setPrimaryClip(clip);
            Log.e("version","2 version");
        }
    }

    // 存ip
    public static void saveDeviceIP(Context context, String ip) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.DEVICE_IP, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.DEVICE_IP, ip).apply();
    }

    public static String getDeviceIP(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DEVICE_IP, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.DEVICE_IP, "125.119.224.148");
    }


    // 存token
    public static void saveUserLoginName(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.LOGIN_USER_NAME, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.LOGIN_USER_NAME, token).apply();
    }

    public static String getUserName(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGIN_USER_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.LOGIN_USER_NAME, "");
    }


    // 存token
    public static void saveUserLoginToken(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.LOGIN_USER_TOKEN, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.LOGIN_USER_TOKEN, token).apply();
    }

    public static String getUserToken(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGIN_USER_TOKEN, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.LOGIN_USER_TOKEN, "");
    }

    // 存api
    public static void saveChangeAPI(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.CALL_API, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.CALL_API, token).apply();
    }

    public static String getChangeAPI(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.CALL_API, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.CALL_API, "");
    }
    // 存公告
    public static void saveUserInfoNews(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.USERINFO_NEWS, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.USERINFO_NEWS, token).apply();
    }

    public static String getUserInfoNews(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USERINFO_NEWS, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.USERINFO_NEWS, "");
    }


    public static void isShowNews(Context context,String getNews){
        String localNews = getUserInfoNews(context);
        if (!localNews.equals(getNews)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("公告");
            alertDialog.setMessage(getNews);
            /*一樣，不熟的用這個打就OK了*/
            alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveUserInfoNews(context,getNews);
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
        }

    }


    public static String md5(String content) {
        byte[] hash;
        String newString = Constant.MD5_String +  content;
        try {
            hash = MessageDigest.getInstance("MD5").digest(newString.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException",e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    // 存公告
    public static void saveBuyMax(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.BUY_MAX, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.BUY_MAX, token).apply();
    }

    public static String getBuyMax(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.BUY_MAX, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.BUY_MAX, "");
    }

    public static void saveBuyMin(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.BUY_MIN, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.BUY_MIN, token).apply();
    }

    public static String getBuyMin(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.BUY_MIN, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.BUY_MIN, "");
    }


    public static void saveBuyIsOpen(Context context, boolean token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.BUY_ISOPEN, Context.MODE_PRIVATE).edit();
        edit.putBoolean(Constant.BUY_ISOPEN, token).apply();
    }

    public static Boolean getBuyIsOpen(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.BUY_ISOPEN, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(Constant.BUY_ISOPEN, true);
    }
    //外部url
    public static void saveOpenUrl(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.OPEN_URL, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.OPEN_URL, token).apply();
    }

    public static String getOpenUrl(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.OPEN_URL, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.OPEN_URL, BuildConfig.API_URL);
    }


    public static void saveRebate(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.USER_REBATE, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.USER_REBATE, token).apply();
    }

    public static String getRebate(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_REBATE, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.USER_REBATE, "");
    }


    public static void saveWechat(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.USER_WECHAT, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.USER_WECHAT, token).apply();
    }

    public static String getWechat(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_WECHAT, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.USER_WECHAT, "");
    }

    public static void saveBank(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.USER_BANK, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.USER_BANK, token).apply();
    }

    public static String getBank(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_BANK, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.USER_BANK, "0");
    }








    public static void savePaymentXeRebate(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.USER_PAYMENTXEREBATE, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.USER_PAYMENTXEREBATE, token).apply();
    }

    public static String getPaymentXeRebate(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_PAYMENTXEREBATE, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.USER_PAYMENTXEREBATE, "");
    }


    public static void saveAlipayRebate(Context context, String token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.USER_AlipayRebate, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.USER_AlipayRebate, token).apply();
    }

    public static String getAlipayRebate(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_AlipayRebate, Context.MODE_PRIVATE);

        return sharedPreferences.getString(Constant.USER_AlipayRebate, "");
    }


    public static void saveSellState(Context context, boolean token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.CHECKSELLSTATE, Context.MODE_PRIVATE).edit();
        edit.putBoolean(Constant.CHECKSELLSTATE, token).apply();
    }

    public static boolean getSellState(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.CHECKSELLSTATE, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(Constant.CHECKSELLSTATE,true);
    }



    public static void saveVideoState(Context context, boolean token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.VIDEOSWITCH, Context.MODE_PRIVATE).edit();
        edit.putBoolean(Constant.VIDEOSWITCH, token).apply();
    }

    public static boolean getVideoState(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.VIDEOSWITCH, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(Constant.VIDEOSWITCH, false);
    }


    public static void savebuyListSize(Context context, Integer token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.BUYLISTSIZE, Context.MODE_PRIVATE).edit();
        edit.putInt(Constant.BUYLISTSIZE, token).apply();
    }

    public static Integer getbuyListSize(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.BUYLISTSIZE, Context.MODE_PRIVATE);

        return sharedPreferences.getInt(Constant.BUYLISTSIZE, 0);
    }




    public static void saveBuyArrayList(Context context,ArrayList<String> list){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(Constant.BUYLIST, json);
        editor.apply();

    }

    public static ArrayList<String> getBuyArrayList(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(Constant.BUYLIST, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }


    public static void saveSellArrayList(Context context,ArrayList<String> list){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(Constant.SELLLIST, json);
        editor.apply();

    }

    public static ArrayList<String> getSellArrayList(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(Constant.SELLLIST, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }



    // 驗證碼
    public static void saveGoogle(Context context, boolean token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.CHECKGOOGLE, Context.MODE_PRIVATE).edit();
        edit.putBoolean(Constant.CHECKGOOGLE, token).apply();
    }

    public static boolean getGoogle(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.CHECKGOOGLE, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(Constant.CHECKGOOGLE, true);
    }

    // quality
    public static void saveQuality(Context context, int token) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.QUALITY, Context.MODE_PRIVATE).edit();
        edit.putInt(Constant.QUALITY, token).apply();
    }

    public static int getQuality(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.QUALITY, Context.MODE_PRIVATE);

        return sharedPreferences.getInt(Constant.QUALITY, 40);
    }


    // 存銀行卡列表 全部
    public static void setSaveALLBankCardData(Context context, List<BanCardListData.Data> bankCardInfo) {
        if (bankCardInfo == null) {
            return;
        }

        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.DEFAULT_SETTING, Context.MODE_PRIVATE).edit();
        edit.putString(Constant.DEFAULT_SETTING_ALL_BANKCARD, new Gson().toJson(bankCardInfo)).apply();
    }


    public static ArrayList<BanCardListData.Data> getALLBankCardData(Context context) {
        ArrayList<BanCardListData.Data> bankCardInfo = new ArrayList<>();
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DEFAULT_SETTING, Context.MODE_PRIVATE);
            String jsonStr = sharedPreferences.getString(Constant.DEFAULT_SETTING_ALL_BANKCARD, "");
            if (!jsonStr.isEmpty()) {
                bankCardInfo = new Gson().fromJson(jsonStr, new TypeToken<List<BanCardListData.Data>>(){}.getType());
            }
//            for (BanCardListData.Data info : bankCardInfo) {
//                info.setOpen(false);
//            }
            return bankCardInfo;
        } catch (Exception e) {
            return bankCardInfo;
        }
    }
}
