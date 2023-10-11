package com.tools.payhelper.pay;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyApplication extends Application {
    private static MyApplication myApplication;
    public static MyApplication getInstance(){
        return myApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        getActivityLifecycleCallbacks();

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void getActivityLifecycleCallbacks(){
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    private  void get(){
        registerComponentCallbacks(new ComponentCallbacks2() {
            @Override
            public void onTrimMemory(int i) {

            }

            @Override
            public void onConfigurationChanged(@NonNull Configuration configuration) {

            }

            @Override
            public void onLowMemory() {

            }
        });
    }


}
