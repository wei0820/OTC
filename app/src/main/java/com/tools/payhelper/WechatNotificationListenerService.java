package com.tools.payhelper;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;


public class WechatNotificationListenerService  extends NotificationListenerService {
    public final  static String TAG=WechatNotificationListenerService.class.getSimpleName();

    /*
       These are the package names of the apps. for which we want to
       listen the notifications
    */
    private static final class ApplicationPackageNames {
        public static final String FACEBOOK_PACK_NAME = "com.facebook.katana";
        public static final String FACEBOOK_MESSENGER_PACK_NAME = "com.facebook.orca";
        public static final String WHATSAPP_PACK_NAME = "com.whatsapp";
        public static final String INSTAGRAM_PACK_NAME = "com.instagram.android";
        public static final String WECHAT_PACK_NAME = "com.tencent.mm";
    }

    /*
        These are the return codes we use in the method which intercepts
        the notifications, to decide whether we should do something or not
     */
    public static final class InterceptedNotificationCode {
        public static final int FACEBOOK_CODE = 9;
        public static final int WHATSAPP_CODE = 2;
        public static final int INSTAGRAM_CODE = 3;
        public static final int OTHER_NOTIFICATIONS_CODE = 4; // We ignore all notification with code == 4
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        int notificationCode = matchNotificationCode(sbn);
        Bundle extras = sbn.getNotification().extras;
        Log.d(TAG,"packageName="+sbn.getPackageName());
        if(!sbn.getPackageName().equals(ApplicationPackageNames.WECHAT_PACK_NAME)) return;



        if (extras!=null){
            //获取通知消息标题
            String title = extras.getString(Notification.EXTRA_TITLE);
            // 获取通知消息内容
            Object msgText = extras.getCharSequence(Notification.EXTRA_TEXT);
//注意：获取的通知信息和短信的传递内容不一样 短信为SpannableString 这里容易造成转换异常
            if (msgText instanceof SpannableString){
                Log.d(TAG,"is SpannableString ...."+((SpannableString) msgText).subSequence(0,((SpannableString) msgText).length()));

            }else{
                Log.d(TAG,"showMsg packageName="+",title="+title+",msgText="+msgText);
                Intent intent = new  Intent("com.github.chagall.notificationlistenerexample");
                intent.putExtra("Notification Code", 1234567);
                intent.putExtra("title", title);
                intent.putExtra("msgText", msgText+"");

                sendBroadcast(intent);
            }

        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        int notificationCode = matchNotificationCode(sbn);
        Bundle extras = sbn.getNotification().extras;
        if(notificationCode != InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE) {

            StatusBarNotification[] activeNotifications = this.getActiveNotifications();

            if(activeNotifications != null && activeNotifications.length > 0) {
                for (int i = 0; i < activeNotifications.length; i++) {
                    if (notificationCode == matchNotificationCode(activeNotifications[i])) {
                        if (extras!=null){
                            //获取通知消息标题
                            String title = extras.getString(Notification.EXTRA_TITLE);
                            // 获取通知消息内容
                            Object msgText = extras.getCharSequence(Notification.EXTRA_TEXT);
//注意：获取的通知信息和短信的传递内容不一样 短信为SpannableString 这里容易造成转换异常
                            if (msgText instanceof SpannableString){
                                Log.d(TAG,"is SpannableString ...."+((SpannableString) msgText).subSequence(0,((SpannableString) msgText).length()));

                            }else{
                                Log.d(TAG,"showMsg packageName="+",title="+title+",msgText="+msgText);


                                Intent intent = new  Intent("com.github.chagall.notificationlistenerexample");
                                intent.putExtra("showMsg", msgText+"");
                                intent.putExtra("Notification Code", notificationCode);
                                sendBroadcast(intent);
                            }

                        }




                        break;
                    }
                }
            }
        }
    }

    private int matchNotificationCode(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();

        if(packageName.equals(ApplicationPackageNames.FACEBOOK_PACK_NAME)
                || packageName.equals(ApplicationPackageNames.FACEBOOK_MESSENGER_PACK_NAME)){
            return(InterceptedNotificationCode.FACEBOOK_CODE);
        }
        else if(packageName.equals(ApplicationPackageNames.INSTAGRAM_PACK_NAME)){
            return(InterceptedNotificationCode.INSTAGRAM_CODE);
        }
        else if(packageName.equals(ApplicationPackageNames.WHATSAPP_PACK_NAME)){
            return(InterceptedNotificationCode.WHATSAPP_CODE);
        }
        else{
            return(InterceptedNotificationCode.OTHER_NOTIFICATIONS_CODE);
        }
    }



}
