package com.jingyu.pay.ui.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jingyu.pay.BasicActivity
import com.tools.payhelper.R
import com.tools.payhelper.SystemUtil
import com.tools.payhelper.UpdateAlertDialog
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.login.MainActivity
import com.tools.payhelper.ui.login.LoginViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class LoginActivity : BasicActivity() {
    val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
    }
    lateinit var edt : EditText
    lateinit var edt2 : EditText
    lateinit var edt3 : EditText
    lateinit var loginButton: Button
    lateinit var _versiontext : TextView

    lateinit var progressDialog: ProgressDialog
    var tokenString = "test"



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginButton = findViewById(R.id.loginbtn)
        edt = findViewById(R.id.edt)
        edt2 = findViewById(R.id.edt2)
        edt3 = findViewById(R.id.edt3)
        _versiontext = findViewById(R.id.vertext);
        _versiontext.text = "当前版本:" + PayHelperUtils.getVersionName() +"\n"+ "当前版本号:"+ PayHelperUtils.getVersionCode()+"\n"+ "当前网址:"+ PayHelperUtils.getOpenUrl(this)
        _versiontext.visibility = View.GONE
        check()
        PayHelperUtils.getLocalIpAddress(this)
        PayHelperUtils.saveBIsOpen(this,false)
        PayHelperUtils.saveSellState(this,false)
        PayHelperUtils.saveTopNews(this,"")

        loginButton.setOnClickListener {
            loginButton.isEnabled = false
            loginButton.isClickable = false;
            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("请稍候")
            progressDialog.setMessage("登入中 ...")
            progressDialog.setCancelable(true) // blocks UI interaction
            progressDialog.show()

            var loginid = edt.text.toString()
            var password = edt2.text.toString()
            var code = edt3.text.toString()


            if (loginid.isEmpty()){
                ToastManager.showToastCenter(this,"帐号不得为空")
                return@setOnClickListener

            }else if (password.isEmpty()){
                ToastManager.showToastCenter(this,"密码不得为空")

                return@setOnClickListener

            }




            loginViewModel.getUserToken(this,loginid,PayHelperUtils.md5(password),code).observe(this, Observer {

                if (it!=null){
                    runOnUiThread {
                        if (it.code==1){
                            if(!it.msg.isEmpty()){
                                ToastManager.showToastCenter(this,it.msg)
                                loginButton.isEnabled = true
                                loginButton.isClickable = true;
                                progressDialog.dismiss()
                                return@runOnUiThread

                            }else{
                                ToastManager.showToastCenter(this,"请求异常 无法无法连线到远程服务器")
                                progressDialog.dismiss()
                                return@runOnUiThread

                            }
                        }else{
                            progressDialog.hide()
                            tokenString = it.data.token
                            ToastManager.showToastCenter(this,it.msg)

                            PayHelperUtils.saveUserLoginToken(this,it.data.token)
                            PayHelperUtils.saveUserLoginName(this,loginid)
                            PayHelperUtils.saveGoogle(this,it.data.google)
                            var intent  = Intent()
                            intent.setClass(this, MainActivity::class.java)
                            startActivity(intent)

                        }

                    }
                }else{

                    runOnUiThread {
                        ToastManager.showToastCenter(this,"请求异常 无法无法连线到远程服务器")
                        loginButton.isEnabled = true
                        loginButton.isClickable = true;
                        progressDialog.dismiss()
                    }

                }


            })



        }


    }
    fun  checkVresion(){
        lifecycleScope.launch {
            loginViewModel._version.collect {
                if (it!=null){
                    if(it.data.quality!=null){
                        PayHelperUtils.saveQuality(this@LoginActivity,it.data.quality)
                    }
                }

                if (PayHelperUtils.getVersionCode()<it.data.versionCode){
                    val dialog = UpdateAlertDialog(this@LoginActivity,it.data.url)
                    dialog.setMessage(String.format("欢迎使用%s原生V%s版本",
                        getString(R.string.app_name),
                        it.data.versionName)+"如升级失败，请选择网页下载升级")
                    dialog.setIsForcedUpdate(true)
                    dialog.show()
            }



            }
        }


    }

    fun check(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissionList = ArrayList<kotlin.String>()
                permissionList.add(Manifest.permission.CAMERA)
                permissionList.add(Manifest.permission.READ_SMS)
                permissionList.add(Manifest.permission.RECEIVE_SMS)
                permissionList.add(Manifest.permission.READ_PHONE_STATE)
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                permissionList.add(Manifest.permission.INTERNET)
                checkAndRequestPermissions(permissionList)
            }
        } catch (ignored: Exception) {

        }
    }
    private fun checkAndRequestPermissions(arrayList: ArrayList<kotlin.String>) {
        val arrayList2: ArrayList<kotlin.String> = ArrayList<kotlin.String>(arrayList)
        val it = arrayList2.iterator()
        while (it.hasNext()) {
            if (checkSelfPermission((it.next() as kotlin.String)) == PackageManager.PERMISSION_GRANTED) {
                it.remove()
            }
        }
        if (arrayList2.size != 0) {
            requestPermissions((arrayList2.toTypedArray() as Array<kotlin.String?>), 1)
        }
    }

    @SuppressLint("WrongConstant")
    fun ignoreBatteryOptimization() {
        try {
            if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (getSystemService("power") as PowerManager).isIgnoringBatteryOptimizations(
                    packageName
                )) || Build.VERSION.SDK_INT < Build.VERSION_CODES.M
            ) {
                val intent = Intent("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS")
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        } catch (ignored: java.lang.Exception) {
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<kotlin.String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if (requestCode == 1) {
                for (i in grantResults.indices) {
                    if (grantResults[i] == 0) {


                    } else {
//                        sendmsg(permissions[i] + " 权限授以失败: " + grantResults[i])
                    }
                }
            }
        } catch (ignored: java.lang.Exception) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // if the intentResult is null then
        // toast a message as "cancelled"

    }

    override fun onResume() {
        super.onResume()
//        checkVresion()

    }
    

}