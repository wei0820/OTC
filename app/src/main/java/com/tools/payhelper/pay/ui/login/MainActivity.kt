package com.tools.payhelper.pay.ui.login

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jingyu.pay.ui.login.LoginViewModel
import com.tools.payhelper.BuildConfig
import com.tools.payhelper.R
import com.tools.payhelper.databinding.ActivityMainBinding
import com.tools.payhelper.pay.ToastManager
import com.tools.payhelper.pay.ui.dashboard.SellListData
import com.tools.payhelper.ui.login.LoginViewModelFactory

class MainActivity : AppCompatActivity(),Handler.Callback, NotifyListener {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1


    private lateinit var binding: ActivityMainBinding
    private  val TAG = "MainActivity"
    val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
    }
    var handler: Handler? = null
    var buyDataList: ArrayList<SellListData.Data> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        handler = Handler(this)
//
        createNot()
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
        getInfo()
//        if(!CheckServiceManager.isListenerEnabled(this)){
//            loginViewModel.postDb(this,"","","","無授權權限給此app").observe(this, Observer {
//
//            })
//        }else{
//
//            loginViewModel.postDb(this,"","","","獲取授權開始監聽").observe(this, Observer {
//
//            })
//        }
//        CheckServiceManager.check(this)


        NotifyHelper.getInstance().setNotifyListener(this)
    }



    override fun onResume() {
        super.onResume()


    }

    override fun onStart() {
        super.onStart()

    }

    override fun onPause() {
        super.onPause()

    }


    override fun onDestroy() {
        super.onDestroy()

        if (handler != null) {
            handler!!.removeCallbacksAndMessages(null);
            handler = null;

        }
    }


    fun getInfo(){

//        lifecycleScope.launch {
//            loginViewModel._version.collect {
//                if (it!=null){
//                    Log.d("MainActivity",it.toString())
//
//                    if (PayHelperUtils.getVersionCode()<it.data.versionCode){
//                        ToastManager.showToastCenter(this@MainActivity,"發現新版本");
//
//
//                    }
//                }
//            }
//        }


        loginViewModel.getUserInfo(this).observe(this, Observer {
            if(it!=null){


                if (!it.data.isEnable){
                    ToastManager.showToastCenter(this,"令牌失效 请重新登入")
                }else{
                    if(it.data.isCollectionQueue!=null){
                        if(!it.data.isCollectionQueue){
                            ToastManager.showToastCenter(this,"卖币已关闭 请重新开启(先关闭在开启) 或 重新登入")

                        }
                    }

                }
            }

        })
        loginViewModel.getCheckList(this).observe(this, Observer{
            buyDataList.clear()
            if (it!=null){

                it.data.forEach {
                    if (it.state==0){
                        buyDataList.add(it)
                    }
                }
            }
            runOnUiThread {
                if (buyDataList.size >=1){
                    sendnot()
                }
            }


        })
        handler!!.sendEmptyMessageDelayed(1,30000)
    }

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
    @SuppressLint("WrongConstant")
    fun createNot(){
        // 確認是否為Android 8.0以上版本
        // 8.0以上版本才需要建立通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            //設定通知渠道名稱、描述和重要性
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("11", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)


        }
    }

    fun sendnot(){



//
        when(BuildConfig.APPLICATION_ID)
        {


                "com.duobao" ->
                {

                    val builder = NotificationCompat.Builder(this, "11")
                        .setSmallIcon(R.drawable.img_duobao)
                        .setContentTitle("你有订单待确认")
                        .setContentText("你有订单待确认")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(Notification.DEFAULT_ALL)


                    with(NotificationManagerCompat.from(this)) {
                        // notificationId is a unique int for each notification that you must define
                        val notificationId = 10
                        notify(notificationId, builder.build())
                    }

            }
            "com.jingyu.otc" ->
                {
        val builder = NotificationCompat.Builder(this, "11")
            .setSmallIcon(R.drawable.img_otc)
            .setContentTitle("你有订单待确认")
            .setContentText("你有订单待确认")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(Notification.DEFAULT_ALL)


        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            val notificationId = 10
            notify(notificationId, builder.build())
        }

                }
            "com.geelyotc.pay" ->
            {

            }

        }

    }

    override fun onReceiveMessage(type: String?) {
        Log.d("onReceiveMessage", type!!)

        if(!type.isEmpty()){
            ToastManager.showToastCenter(this,type!!)

            loginViewModel.postDb(this,"","","",type).observe(this, Observer {
                runOnUiThread {
                    ToastManager.showToastCenter(this,it.msg)

                }

            })
        }else{

        }


    }

    override fun onRemovedMessage(type: Int) {
    }
}