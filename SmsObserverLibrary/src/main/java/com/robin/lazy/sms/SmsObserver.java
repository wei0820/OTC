package com.robin.lazy.sms;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/***
 * 短信接收观察者
 *
 * @author 江钰锋 0152
 * @version [版本号, 2015年9月17日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SmsObserver extends ContentObserver {

    private Context mContext;
    public static final int MSG_RECEIVED_CODE = 1001;
    private SmsHandler mHandler;

    /***
     * 构造器
     * @param context
     * @param callback 短信接收器
     * @param smsFilter 短信过滤器
     */
    public SmsObserver(Activity context, SmsResponseCallback callback,SmsFilter smsFilter) {
        this(new SmsHandler(callback,smsFilter));
        this.mContext = context;
    }

    public SmsObserver(Activity context, SmsResponseCallback callback) {
        this(new SmsHandler(callback));
        this.mContext = context;
    }

    public SmsObserver(SmsHandler handler) {
        super(handler);
        this.mHandler = handler;
    }

    /***
     * 设置短信过滤器
     * @param smsFilter
     */
    public void setSmsFilter(SmsFilter smsFilter) {
        mHandler.setSmsFilter(smsFilter);
    }

    /***
     * 注册短信变化观察者
     *
     * @see [类、类#方法、类#成员]
     */
    public void registerSMSObserver() {
        Uri uri = Uri.parse("content://sms");
        if (mContext != null) {
            mContext.getContentResolver().registerContentObserver(uri,
                    true, this);
        }
    }

    /***
     * 注销短信变化观察者
     *
     * @see [类、类#方法、类#成员]
     */
    public void unregisterSMSObserver() {
        if (mContext != null) {
            mContext.getContentResolver().unregisterContentObserver(this);
        }
        if (mHandler != null) {
            mHandler = null;
        }
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Log.i("[SMS]", "sms uri = " + uri.toString());
        if (uri.toString().equals("content://sms/raw")) {
            Log.i("[SMS]", "raw inbox are empty --> return");
            return;
        }
        if (uri.toString().contains("content://sms/raw")) {
            Log.i("[SMS]", "in the raw inbox --> return");
            return;
        }

        try {
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, "date desc");
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    ContentValues values = new ContentValues();
                    values.put("read", "1");

                    String address = cursor.getString(cursor.getColumnIndex("address"));
                    String body = cursor.getString(cursor.getColumnIndex("body"));
                    if (mHandler != null) {
                        mHandler.obtainMessage(MSG_RECEIVED_CODE, new String[]{body, address}).sendToTarget();
                    }

                    Log.i("[SMS]", "发件人为：" + address + " " + "短信内容为：" + body);
                }
                cursor.close();
            }
        } catch (SecurityException e) {
            Log.e("[SMS]", "获取短信权限失败 : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
