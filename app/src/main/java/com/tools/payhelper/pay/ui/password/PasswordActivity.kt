package com.jingyu.pay.ui.password

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jingyu.pay.BasicActivity
import com.jingyu.pay.MainActivity
import com.jingyu.pay.ui.order.PassWordViewModel
import com.jingyu.pay.ui.order.PasswordViewModelFactory
import com.tools.payhelper.R
import com.tools.payhelper.UpdateAlertDialog
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.login.AddGoogleDialog

import kotlinx.coroutines.launch


class PasswordActivity : BasicActivity() {
    val passWordViewModel: PassWordViewModel by lazy {
        ViewModelProvider(this, PasswordViewModelFactory()).get(PassWordViewModel::class.java)
    }
    lateinit var edt : EditText
    lateinit var edt2 : EditText
    lateinit var edt3 : EditText
    lateinit var loginButton: Button
    lateinit var closbutton : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        loginButton = findViewById(R.id.loginbtn)
        edt = findViewById(R.id.edt)
        edt2 = findViewById(R.id.edt2)
        edt3 = findViewById(R.id.edt3)
        closbutton = findViewById(R.id.closeBtn)
        closbutton.setOnClickListener {
            this@PasswordActivity.finish()
        }


        loginButton.setOnClickListener {
            var loginid = edt.text.toString()
            var password = edt2.text.toString()
            var code = edt3.text.toString()


            if (loginid.isEmpty()){
                ToastManager.showToastCenter(this,"密码不得为空")
                return@setOnClickListener

            }else if (password.isEmpty()){
                ToastManager.showToastCenter(this,"密码不得为空")

                return@setOnClickListener

            }else if (code.isEmpty()){
                ToastManager.showToastCenter(this,"验证码不得为空")

                return@setOnClickListener

            }

            passWordViewModel.getSecurityData(this,PayHelperUtils.md5(loginid),password,code).observe(this,
                Observer {
                    lifecycleScope.launch {
                        if (it!=null){
                            if (it.code == 0){
                                ToastManager.showToastCenter(this@PasswordActivity,it.msg)
                                this@PasswordActivity.finish()
                            }else{
                                ToastManager.showToastCenter(this@PasswordActivity,it.msg)
                            }

                        }
                    }
                })





        }


    }


}