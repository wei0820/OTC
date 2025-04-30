package com.tools.payhelper.pay.ui.login

import android.content.Context
import android.os.Build
import android.provider.Settings
object DeviceInfoUtils {

    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "UNKNOWN"
    }

    fun getDeviceBrand(): String {
        return Build.BRAND ?: "UNKNOWN"
    }

    fun getDeviceModel(): String {
        return Build.MODEL ?: "UNKNOWN"
    }

    fun getAndroidVersion(): String {
        return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }

}