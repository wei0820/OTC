package com.tools.payhelper.pay.ui.login

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jingyu.pay.ui.login.LoginViewModel
import com.tools.payhelper.R
import com.tools.payhelper.UpdateAlertDialog
import com.tools.payhelper.databinding.ActivityMainBinding
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.ui.login.LoginViewModelFactory
import java.lang.String

class MainActivity : AppCompatActivity(),Handler.Callback{

    private lateinit var binding: ActivityMainBinding
    private  val TAG = "MainActivity"
    val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
    }
    var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_notifications,  R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.selectedItemId = R.id.navigation_notifications


    }

    override fun onResume() {
        super.onResume()
        if (handler != null){

        }else{
            handler = Handler(this)

        }
        getInfo()


    }


    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null);
            handler = null;

        }
    }


    fun getInfo(){
        Log.d("Jack","getinfo")
        loginViewModel.getUserInfo(this).observe(this, Observer {
            if (it!=null){
                runOnUiThread {
//                    if (!it.data.isEnable){
//                        ToastManager.showToastCenter(this,"令牌失效 请重新登入")
//                    }
                }

            }
        })
        handler!!.sendEmptyMessageDelayed(1,15000)
    }
//    fun getUpdate(){
//        loginViewModel.getVersionUpdate(this).observe(this, Observer {
//            if (it!=null){
//                if (PayHelperUtils.getVersionCode()<it.data.versionCode){
//                    val dialog = UpdateAlertDialog(this@MainActivity,it.data.url)
//                    dialog.setMessage(String.format("欢迎使用%s原生V%s版本",
//                        getString(R.string.app_name),
//                        it.data.versionName)+"如升级失败，请选择网页下载升级")
//                    dialog.setIsForcedUpdate(true)
//                    dialog.show()
//                }
//
//
//            }
//        })
//    }

    override fun onRestart() {
        super.onRestart()

    }

    override fun onResumeFragments() {
        super.onResumeFragments()

    }

    override fun handleMessage(p0: Message): Boolean {
        if (p0.what ==1){
            getInfo()
        }
        return false;

    }
}