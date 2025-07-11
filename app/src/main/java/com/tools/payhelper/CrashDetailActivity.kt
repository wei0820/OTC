package com.tools.payhelper.pay

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tools.payhelper.R

import java.io.File


class CrashDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_detail)

        val textView = findViewById<TextView>(R.id.crashTextView)
        val copyButton = findViewById<Button>(R.id.copyButton)
        val clearButton = findViewById<Button>(R.id.clearButton)

        val crashFile = File(filesDir, "crash_log.txt")
        val crashText = try {
            crashFile.readText()
        } catch (e: Exception) {
            "無法讀取錯誤紀錄"
        }

        textView.text = crashText

        copyButton.setOnClickListener {


            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("CrashLog", crashText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "已複製到剪貼簿", Toast.LENGTH_SHORT).show()
        }

        clearButton.setOnClickListener {
            if (crashFile.exists()) {
                crashFile.delete()
                Toast.makeText(this, "紀錄已刪除", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}