package com.tools.payhelper.pay.ui.login

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jingyu.pay.ui.login.LoginViewModel
import com.tools.payhelper.R
import com.tools.payhelper.databinding.ActivityMainBinding
import com.tools.payhelper.pay.PayHelperUtils
import com.tools.payhelper.ui.login.LoginViewModelFactory

class MainActivity : AppCompatActivity()  , Handler.Callback{

    private lateinit var binding: ActivityMainBinding
    private  val TAG = "MainActivity"
    var googleBoolean = false;
    val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
    }
    var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        handler = Handler(this)


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
//        getData()
        getInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null);
            handler = null;

        }
    }

    fun  getData() : Boolean{
        return  intent.getBooleanExtra("google",false)

    }

    fun getInfo(){
        loginViewModel.getUserInfo(this)
        handler!!.sendEmptyMessageDelayed(1,10000)
    }

    override fun handleMessage(p0: Message): Boolean {
        if (p0.what ==1){
//            if(PayHelperUtils.getSellState(requireActivity())){
//                openSell()
//
//            }else{
//                Log.d("openSell","close")
//            }
            getInfo()

        }
        return false;

    }
}