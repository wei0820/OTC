package com.tools.payhelper;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;



public class DaemonService extends Service {
    public static final int NOTICE_ID = 100;
    String CHANNEL_ONE_ID = "com.tools.payhelper";
    public DaemonService(){}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("jack","DaemonService");

        //如果API大于18，需要弹出一个可见通知  
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            Notification.Builder builder = new Notification.Builder(this);
//            builder.setSmallIcon(R.drawable.ic_launcher);
//            builder.setContentTitle(getResources().getString(R.string.appname));
//            builder.setContentText(String.format("%s正在运行中...", getResources().getString(R.string.appname)));
//            builder.setAutoCancel(false);
//            builder.setChannelId(CHANNEL_ONE_ID);
//            builder.setOngoing(true);
//            startForeground(NOTICE_ID, builder.build());
//        } else {
//            startForeground(NOTICE_ID, new Notification());
//        }
        // 将该服务转为前台服务
        // 需要设置 ID 和 通知
        // 设置 ID 为 0 , 就不显示已通知了 , 但是 oom_adj 值会变成后台进程 11
        // 设置 ID 为 1 , 会在通知栏显示该前台服务
        //startForeground(1, new Notification());
        startForeground();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 如果Service被终止  
        // 当资源允许情况下，重启service  
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 如果Service被杀死，干掉通知
        Log.d("jack","任务被杀死");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            if (notificationManager != null) {
//                notificationManager.cancel(NOTICE_ID);
//            }
//        }

        // 重启自己  
//        Intent intent = new Intent(getApplicationContext(), DaemonService.class);
//        startService(intent);

    }
    /**
     * 启动前台服务
     */
    private void startForeground() {
        String channelId = null;
        // 8.0 以上需要特殊处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel("kim.hsl", "ForegroundService");
        } else {
            channelId = "";
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        Notification notification = builder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getResources().getString(R.string.appname))
                .setContentText(String.format("%s正在运行中...", getResources().getString(R.string.appname)))
                .setPriority(PRIORITY_MIN)
                .setAutoCancel(false)
                .setOngoing(true)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(1, notification);
    }

    /**
     * 创建通知通道
     * @param channelId
     * @param channelName
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){
        @SuppressLint("WrongConstant") NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }
}  