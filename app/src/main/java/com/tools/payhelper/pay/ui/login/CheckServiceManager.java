package com.tools.payhelper.pay.ui.login;

import static android.text.TextUtils.isEmpty;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

public class CheckServiceManager {
    public  static  Intent intent ;

    public  static  void check(Activity activity){
        //请求监听权限
        if (!isListenerEnabled(activity)){   //这里的isListenerEnabled用于判断权限是否获取
//若已经获取权限则不执行下面的代码
            intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            activity.startActivity(intent);//启动权限获取界面
        }
        //启动监听服务
        intent = new Intent(activity,MyNotificationListenerService.class);
        activity.startService(intent);
    }
    //判断监听权限是否开启
    public static boolean isListenerEnabled(Activity activity) {
        String pkgName = activity.getPackageName();
        final String flat = Settings.Secure.getString(activity.getContentResolver(), "enabled_notification_listeners");
        if (!isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        Log.d("MyNotificationListenerService","NotificationListener ON");
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
