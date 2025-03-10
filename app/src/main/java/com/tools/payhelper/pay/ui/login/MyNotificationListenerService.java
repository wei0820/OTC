package com.tools.payhelper.pay.ui.login;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import com.jingyu.pay.ui.login.LoginViewModel;
import com.tools.payhelper.pay.ui.login.MainActivity;

public class MyNotificationListenerService extends NotificationListenerService {
    //继承NotificationListenerService类
    @Override
    public void onCreate() {
        super.onCreate();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //这部分的作用是在服务被杀死后重新启动服务，防止系统杀后台导致监听服务不能持续运行。
        return START_STICKY;
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // 当有新通知时调用
        Notification notification = sbn.getNotification();
        // 获取通知内容
        String notificationContent = notification.extras.getString(Notification.EXTRA_TEXT);
        String packageName = sbn.getPackageName();//包名
        String notificationKey = sbn.getKey();

        // 你可以在这里添加自己的逻辑来处理通知
        if (notificationContent !=null){
            Log.d("MyNotificationListenerService",packageName);//打印出来内容
            Log.d("MyNotificationListenerService",notificationContent);//打印出来内容

            if (packageName.equals("cn.gov.pbc.dcep")){
                //这里就已经成功获取了
                //notificationContent即为获取到的通知内容
                Log.d("MyNotificationListenerService",packageName);//打印出来内容

                Log.d("MyNotificationListenerService",notificationContent);//打印出来内容
                NotifyHelper.getInstance().onReceive(notificationContent);
            }

        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // 当通知被移除时调用
    }

    @Override
    public void onDestroy() {
//服务被销毁时的逻辑
    }

    //发送或存储通知数据的逻辑
    public void send(){

    }
}
