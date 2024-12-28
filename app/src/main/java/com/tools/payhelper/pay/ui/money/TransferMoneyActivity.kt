package com.tools.payhelper.pay.ui.money

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jingyu.pay.ui.group.GroupViewModel
import com.jingyu.pay.ui.group.GroupViewModelFactory
import com.jingyu.pay.ui.notifications.TransferMoneyViewModel
import com.jingyu.pay.ui.notifications.TransferMoneyViewModelFactory
import com.tools.payhelper.R
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ToastManager

class TransferMoneyActivity : AppCompatActivity() {
    lateinit var  mMainAccountText : TextView
    lateinit var okBtn : Button
    lateinit var closeBtn : Button
    val transferMoneyViewModel: TransferMoneyViewModel by lazy {
        ViewModelProvider(this, TransferMoneyViewModelFactory()).get(TransferMoneyViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContentView(R.layout.add_tran_money)
        checkUserinfo()
        mMainAccountText = findViewById(R.id.nameedt)
        mMainAccountText.text = PayHelperUtils.getUserName(this)
        closeBtn = findViewById(R.id.closeBtn)
        closeBtn.setOnClickListener {
            this.finish()
        }
        okBtn = findViewById(R.id.okBtn)

    }


    fun checkUserinfo(){
        transferMoneyViewModel.getUserInfo(this).observe(this, Observer {
            if (it!=null){
                runOnUiThread {
                    if (!it.data.isEnable){
                        ToastManager.showToastCenter(this,"令牌失效 请重新登入")
                    }
                }

            }else{
                ToastManager.showToastCenter(this,"令牌失效 请重新登入")

            }
        })

    }
}