package com.jingyu.pay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jingyu.pay.ui.login.LoginActivity
import com.tools.payhelper.R
import com.tools.payhelper.pay.PayHelperUtils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        //啟動執行序
        Thread {
            try {
                Thread.sleep(2000)
                JumpPage()

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()


    }

    fun JumpPage(){
        var token = PayHelperUtils.getUserToken(this)
        val intent  = Intent()
        intent.setClass(this, LoginActivity::class.java)
        startActivity(intent)
//        if (token.isEmpty()){
//
//
//
//        }else{
//            val intent  = Intent()
//            intent.setClass(this, MainActivity::class.java)
//            startActivity(intent)
//        }


    }

}