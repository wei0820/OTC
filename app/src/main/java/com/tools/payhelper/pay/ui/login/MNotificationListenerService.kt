package com.tools.payhelper.pay.ui.login

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.jingyu.pay.ui.login.LoginViewModel
import com.tools.payhelper.ui.login.LoginViewModelFactory

class MNotificationListenerService : NotificationListenerService() {
    //继承NotificationListenerService类

    override fun onCreate() {
        super.onCreate()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //这部分的作用是在服务被杀死后重新启动服务，防止系统杀后台导致监听服务不能持续运行。
        return START_STICKY
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // 当有新通知时调用
        val notification: Notification? = sbn.notification
        // 获取通知内容
        val notificationContent: String? = notification?.extras?.getString(Notification.EXTRA_TEXT)
        val packageName = sbn.packageName //包名
        val notificationKey = sbn.key

        // 你可以在这里添加自己的逻辑来处理通知
        notificationContent?.let {
            //这里就已经成功获取了
            //notificationContent即为获取到的通知内容
            Log.d("MyNotificationListenerService", it) //打印出来内容
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // 当通知被移除时调用
    }

    override fun onDestroy() {
        //服务被销毁时的逻辑
    }

    //发送或存储通知数据的逻辑
    fun send() {

    }
}

