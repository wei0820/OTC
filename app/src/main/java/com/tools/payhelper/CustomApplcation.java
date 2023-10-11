package com.tools.payhelper;


import android.content.Context;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.tencent.bugly.crashreport.CrashReport;


/**
 * 自定义全局Applcation类
 *
 * @author smile
 * @ClassName: CustomApplcation
 * @Description: TODO
 * @date 2014-5-19 下午3:25:00
 */
public class CustomApplcation extends MultiDexApplication {

    public static CustomApplcation mInstance;
    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
//        new PercentageScreenHelper(this, 375).activate();//初始化pt，屏幕适配，将pt转为像素
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mInstance = this;
        CrashReport.initCrashReport(getApplicationContext(), "7f2bcf2301", false);

//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

    public static CustomApplcation getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return context;
    }
}
