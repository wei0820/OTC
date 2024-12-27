package com.tools.payhelper.pay.ui.money

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tools.payhelper.R
import com.tools.payhelper.pay.PayHelperUtils

class TransferMoneyActivity : AppCompatActivity() {
    lateinit var  mMainAccountText : TextView
    lateinit var okBtn : Button
    lateinit var closeBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContentView(R.layout.add_tran_money)
        mMainAccountText = findViewById(R.id.nameedt)
        mMainAccountText.text = PayHelperUtils.getUserName(this)
        closeBtn = findViewById(R.id.closeBtn)
        closeBtn.setOnClickListener {
            this.finish()
        }
        okBtn = findViewById(R.id.okBtn)

    }
}