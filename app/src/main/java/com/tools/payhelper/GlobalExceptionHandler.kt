package com.tools.payhelper

import android.content.Context
import android.os.Build
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GlobalExceptionHandler(
    private val context: Context
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val crashTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val crashInfo = buildString {
            appendLine("===== APP 發生未捕捉例外 =====")
            appendLine("時間：$crashTime")
            appendLine()
            appendLine("【錯誤資訊】")
            appendLine(getStackTraceString(throwable))
            appendLine()
            appendLine("【裝置資訊】")
            appendLine(getDeviceInfo(context))
        }

        // 儲存到本機檔案
        saveCrashToFile(crashInfo)

        // 稍作延遲，確保寫入完成
        Thread.sleep(500)

        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(1)
    }

    private fun saveCrashToFile(log: String) {
        val file = File(context.filesDir, "crash_log.txt")
        file.writeText(log)
    }

    private fun getStackTraceString(t: Throwable): String {
        val sw = StringWriter()
        t.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }

    private fun getDeviceInfo(context: Context): String {
        val metrics = context.resources.displayMetrics
        val resolution = "${metrics.widthPixels}x${metrics.heightPixels}"

        return """
            品牌: ${Build.BRAND}
            型號: ${Build.MODEL}
            製造商: ${Build.MANUFACTURER}
            Android 版本: ${Build.VERSION.RELEASE}
            SDK: ${Build.VERSION.SDK_INT}
            裝置名稱: ${Build.DEVICE}
            顯示版本: ${Build.DISPLAY}
            主機板: ${Build.BOARD}
            架構: ${Build.SUPPORTED_ABIS.joinToString(", ")}
            硬體: ${Build.HARDWARE}
            解析度: $resolution
            Fingerprint: ${Build.FINGERPRINT}
        """.trimIndent()
    }
}
